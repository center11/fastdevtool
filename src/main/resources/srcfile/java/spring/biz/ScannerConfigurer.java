package com.example.middleware.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;
import java.io.IOException;

/**
 * 动态注册bean示例: 生成ScannerConfigurer后，在@Configuration类中使用@Bean注册该bean
 * 使用场景：需要根据配置动态注册bean，比如mybatis扫描dao接口生成代理类并注册代理类bean
 *
 */
public class ScannerConfigurer implements BeanDefinitionRegistryPostProcessor {
    private final Logger logger = LoggerFactory.getLogger(ScannerConfigurer.class);

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        try {
            // 设置要扫描的class文件路径，例如classpath*:cn/demo/dao/**/*.class
            String basePackage = "cn/demo/dao";
            String packageSearchPath = "classpath*:" + basePackage.replace('.', '/') + "/**/*.class";

            // 根据path得到Resource
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);

            for (Resource resource : resources) {
                // 根据元数据创建bean definition
                MetadataReader metadataReader = new SimpleMetadataReaderFactory().getMetadataReader(resource);
                ScannedGenericBeanDefinition beanDefinition = new ScannedGenericBeanDefinition(metadataReader);
                String beanName = Introspector.decapitalize(ClassUtils.getShortName(beanDefinition.getBeanClassName()));

                // 设置bean definition属性
                beanDefinition.setResource(resource);
                beanDefinition.setSource(resource);
                beanDefinition.setScope("singleton");
                beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanDefinition.getBeanClassName());
                beanDefinition.setBeanClass(TestBeanProxy.class);

                // 注册bean definition
                BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
                registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }

    /**
     * 示例代理类
     */
    private static class TestBeanProxy {
        // 代理接口
        private String proxyInterface;
        TestBeanProxy(String proxyInterface){
            this.proxyInterface = proxyInterface;
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
