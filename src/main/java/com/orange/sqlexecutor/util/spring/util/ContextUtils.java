package com.orange.sqlexecutor.util.spring.util;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;

/**
 * Created with IDEA
 * author:licheng
 * Date:2019/9/21
 * Time:上午11:55
 **/
public class ContextUtils {

    public static ApplicationContext applicationContext;
    public static BeanFactory beanFactory;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public static void setBeanFactory(BeanFactory beanFactory) {
        ContextUtils.beanFactory = beanFactory;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ContextUtils.applicationContext = applicationContext;
    }

    public static <T> T registryBean(String beanName, Class<T> clazz, Object targetBean) {
        if(applicationContext.containsBean(beanName)) {
            Object bean = applicationContext.getBean(beanName);
            if (bean.getClass().isAssignableFrom(clazz)) {
                return (T) bean;
            } else {
                throw new RuntimeException("BeanName 重复 " + beanName);
            }
        }

        ((DefaultSingletonBeanRegistry) beanFactory).registerSingleton(beanName, targetBean);
        return applicationContext.getBean(beanName, clazz);
    }

    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> t) {
        return applicationContext.getBean(t);
    }
}
