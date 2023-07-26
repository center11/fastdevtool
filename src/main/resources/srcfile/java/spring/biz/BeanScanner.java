package com.example.testproj.biz;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/**
 * 使用场景：比如封装spring starter，需要获取自定义注解，然后读取注解配置进行bean的创建
 */
@Component
public class BeanScanner implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clz = AopUtils.getTargetClass(bean);
        Method[] methods = clz.getMethods();
        for(Method method : methods){
            TestCustomAnnotation customAnnotation = method.getAnnotation(TestCustomAnnotation.class);
            if(null == customAnnotation){
                continue;
            }
            // 获取注解配置进行业务操作
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    /**
     * 自定义注解示例
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface TestCustomAnnotation {
    }
}
