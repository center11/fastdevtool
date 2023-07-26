package pers.zhf.fastdevtool.ui;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;

/**
 * 速查手册按钮事件
 */
public class HelpBookAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // 打开速查手册UI表单
        Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        ShowSettingsUtil.getInstance().editConfigurable(project, new HelpBookForm());
    }
}
