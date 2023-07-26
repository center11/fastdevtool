package pers.zhf.fastdevtool.service;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;

/**
 * 代码生成接口
 */
public interface CodeGeneratorService {
    void generateCode(PsiDirectory directory, Project project, String chooseNodeName);
}
