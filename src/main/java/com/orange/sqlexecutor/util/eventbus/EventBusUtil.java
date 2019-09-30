package com.orange.sqlexecutor.util.eventbus;

import com.google.common.eventbus.AsyncEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created with IDEA
 * author:licheng
 * Date:2019/9/5
 * Time:下午7:34
 **/
@Component
public class EventBusUtil {

    Logger logger = LoggerFactory.getLogger(EventBusUtil.class);

    @Autowired
    private AsyncEventBus asyncEventBus;

    //注册这个监听器
    public void register(EventBusListener eventBusListener) {
        logger.info("监听器" + eventBusListener.getClass().getName() + "注册成功");
        asyncEventBus.register(eventBusListener);
    }

    public void send(EventInfo event) {
        asyncEventBus.post(event);
    }
}
