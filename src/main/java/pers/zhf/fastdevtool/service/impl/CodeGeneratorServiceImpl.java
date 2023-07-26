package pers.zhf.fastdevtool.service.impl;

import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.impl.LoadTextUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.util.ResourceUtil;
import org.apache.commons.lang.StringUtils;
import pers.zhf.fastdevtool.enums.CategoryTreeNodeEnum;
import pers.zhf.fastdevtool.service.CodeGeneratorService;

import java.net.URL;

public class CodeGeneratorServiceImpl implements CodeGeneratorService {
    @Override
    public void generateCode(PsiDirectory directory, Project project, String chooseNodeName) {
        // 根据选择的分类节点获取需要拷贝的源文件目录和文件名
        CategoryTreeNodeEnum categoryTreeNodeEnum = CategoryTreeNodeEnum.getEnumByName(chooseNodeName);
        if(null == categoryTreeNodeEnum){
            return;
        }

        // 拷贝文件
        this.copyFiles(directory, project, categoryTreeNodeEnum.getSrcFilePath(), categoryTreeNodeEnum.getSrcFileNames().split(","));
    }

    private void copyFiles(PsiDirectory directory, Project project, String srcFilePath, String[] srcFileNameArr) {
        if (StringUtils.isBlank(srcFilePath) || null == srcFileNameArr) {
            return;
        }
        PsiFileFactory psiFileFactory = PsiFileFactory.getInstance(project);
        for (String srcFileName : srcFileNameArr) {
            URL url = ResourceUtil.getResource(getClass().getClassLoader(), srcFilePath, srcFileName);
            VirtualFile srcFile = VfsUtil.findFileByURL(url);
            if(null == srcFile){
                continue;
            }
            PsiFile file = psiFileFactory.createFileFromText(
                    srcFile.getName(),
                    XMLLanguage.INSTANCE,
                    LoadTextUtil.loadText(srcFile)
            );
            WriteCommandAction.runWriteCommandAction(project, () -> {
                directory.copyFileFrom(srcFile.getName(), file);
            });
        }
    }
}
