package com.orange.sqlexecutor.util.spring.init;

import com.orange.sqlexecutor.service.DBInfoManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.ExecutorService;

/**
 * Created with IDEA
 * author:licheng
 * Date:2019/9/23
 * Time:上午1:34
 **/
@WebListener
public class MyServletContextListener implements ServletContextListener {

    Logger logger = LoggerFactory.getLogger(MyServletContextListener.class);

    @Autowired
    private DBInfoManagerService dbInfoManagerService;

    @Autowired
    private ExecutorService cachedThreadPool;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("系统初始化完成");

        /*logger.info("数据连接初始化开始...");
        dbInfoManagerService.initDBLinks();
        logger.info("数据连接初始化完成...");*/

        cachedThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000); // 等待项目启动后延迟10S初始化数据连接
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("数据连接初始化开始...");
                dbInfoManagerService.initDBLinks();
                logger.info("数据连接初始化完成...");
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
