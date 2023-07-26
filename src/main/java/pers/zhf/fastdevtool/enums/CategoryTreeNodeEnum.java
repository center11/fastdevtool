package pers.zhf.fastdevtool.enums;

import java.util.Arrays;

/**
 * 代码生成-分类树节点枚举
 */
public enum CategoryTreeNodeEnum {
    ROOT("1", "",  "全部", "", ""),

    SPRING_VIEW("1_1", "1",  "Spring展示层", "", ""),
    SPRING_VIEW_MVC_INTERCEPT("1_1_1", "1_1",  "Web拦截", "srcfile/java/spring/view", "WebMVCConfig.java"),

    SPRING_BIZ("1_2", "1",  "Spring业务层", "", ""),
    SPRING_BIZ_DYNAMIC_BEAN_REGISTRY("1_2_1", "1_2",  "动态注册bean", "srcfile/java/spring/biz", "ScannerConfigurer.java"),
    SPRING_BIZ_BEAN_SCANNER("1_2_2", "1_2",  "扫描注解", "srcfile/java/spring/biz", "BeanScanner.java"),

    SPRING_DAO("1_3", "1", "Spring持久层", "", ""),
    SPRING_DAO_MYBATIS_PLUS_INTERCEPTOR("1_3_1", "1_3",  "MybatisPlus拦截器", "srcfile/java/spring/dao", "MybatisPlusConfig.java");


    /**
     * 节点id
     **/
    private final String id;
    /**
     * 父节点id
     **/
    private final String parentId;

    /**
     * 节点名称
     **/
    private final String name;
    /**
     * 源文件目录路径
     **/
    private final String srcFilePath;
    /**
     * 源文件目录下的文件名，多个逗号分割
     **/
    private final String srcFileNames;

    CategoryTreeNodeEnum(String id, String parentId, String name,
                         String srcFilePath, String srcFileNames) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.srcFilePath = srcFilePath;
        this.srcFileNames = srcFileNames;
    }

    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }

    public String getSrcFilePath() {
        return srcFilePath;
    }

    public String getSrcFileNames() {
        return srcFileNames;
    }

    public static CategoryTreeNodeEnum getEnumByName(String name) {
        return Arrays.stream(values()).filter(e->e.getName().equals(name)).findFirst().orElse(null);
    }
}
