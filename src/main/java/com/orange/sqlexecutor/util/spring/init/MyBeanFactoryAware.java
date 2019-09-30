package com.orange.sqlexecutor.util.spring.init;

import com.orange.sqlexecutor.util.spring.util.ContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

/**
 * Spring 在实例化bean时会帮我们注入BeanFactory
 *
 * Created by licheng on 2018/10/16.
 */
@Component
public class MyBeanFactoryAware implements BeanFactoryAware {

    Logger logger = LoggerFactory.getLogger(MyBeanFactoryAware.class);

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        logger.info("spring初始化完成，获取beanFactory将其赋值给ContextUtils");
        ContextUtils.setBeanFactory(beanFactory);
    }
}
