package com.orange.sqlexecutor.util.spring.init;

import com.orange.sqlexecutor.util.spring.util.ContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring 在实例化bean时会帮我们注入ApplicationContext
 *
 * Created by licheng on 2018/10/16.
 */
@Component
public class MyApplicationContextAware implements ApplicationContextAware {

    Logger logger = LoggerFactory.getLogger(MyApplicationContextAware.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("spring初始化完成，获取applicationContext将其赋值给ContextUtils");
        ContextUtils.setApplicationContext(applicationContext);
    }
}
