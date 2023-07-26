package pers.zhf.fastdevtool.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import pers.zhf.fastdevtool.enums.CategoryTreeNodeEnum;
import pers.zhf.fastdevtool.service.CodeGeneratorService;
import pers.zhf.fastdevtool.service.impl.CodeGeneratorServiceImpl;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CodeGeneratorForm implements Configurable {
    private static final String FORM_TITLE = "代码生成";
    private JPanel mainPanel;
    private JTree categoryTree;
    private final PsiDirectory directory;
    private final Project project;
    private final CodeGeneratorService codeGeneratorService;

    public CodeGeneratorForm(PsiDirectory directory, Project project) {
        // 变量初始化
        this.directory = directory;
        this.project = project;
        this.codeGeneratorService = new CodeGeneratorServiceImpl();

        // 控件监听
        categoryTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 设置只支持节点单选
                TreePath pathForLocation = categoryTree.getPathForLocation(e.getX(), e.getY());
                categoryTree.setSelectionPath(pathForLocation);

                // 非叶子节点不可选
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) categoryTree.getLastSelectedPathComponent();
                if (null != node && !node.isLeaf()) {
                    categoryTree.clearSelection();
                }
            }
        });
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return FORM_TITLE;
    }

    @Override
    public @Nullable JComponent createComponent() {
        return mainPanel;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() {
        // 点击配置窗体的OK按钮进行代码生成
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) categoryTree.getLastSelectedPathComponent();
        if(null == node){
            return;
        }
        this.codeGeneratorService.generateCode(this.directory, this.project, node.toString());
    }

    private void createUIComponents() {
        // 初始化分类树
        initCategoryTreeUI();
    }

    private void initCategoryTreeUI() {
        // 创建节点
        CategoryTreeNodeEnum[] nodeEnums = CategoryTreeNodeEnum.values();
        Map<String, DefaultMutableTreeNode> idNodeMap = new HashMap<>(nodeEnums.length);
        Map<String, DefaultMutableTreeNode> idAndParentIdNodeMap = new TreeMap<>();
        for (CategoryTreeNodeEnum nodeEnum : nodeEnums) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeEnum.getName());
            idNodeMap.put(nodeEnum.getId(), node);
            idAndParentIdNodeMap.put(nodeEnum.getId() + "," + nodeEnum.getParentId(), node);
        }

        // 关联节点
        DefaultMutableTreeNode root = null;
        for (String idAndParentId : idAndParentIdNodeMap.keySet()) {
            DefaultMutableTreeNode currentNode = idAndParentIdNodeMap.get(idAndParentId);
            String[] arr = idAndParentId.split(",");
            if (arr.length == 1) {
                root = currentNode;
            } else {
                String parentId = arr[1];
                DefaultMutableTreeNode parentNode = idNodeMap.get(parentId);
                parentNode.add(currentNode);
            }

        }

        // 创建树
        DefaultTreeModel model = new DefaultTreeModel(root);
        categoryTree = new Tree(model);
    }
}
