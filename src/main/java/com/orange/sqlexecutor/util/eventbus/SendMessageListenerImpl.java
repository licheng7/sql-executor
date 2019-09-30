package com.orange.sqlexecutor.util.eventbus;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created with IDEA
 * author:licheng
 * Date:2019/9/21
 * Time:下午12:00
 **/
@Component
public class SendMessageListenerImpl implements EventBusListener<SendMsgEventInfo> {

    Logger logger = LoggerFactory.getLogger(SendMessageListenerImpl.class);

    @Autowired
    private EventBusUtil eventBusUtil;

    @Override
    @Subscribe
    public void listener(SendMsgEventInfo info) {

    }

    @Override
    public void registry() {
        eventBusUtil.register(this);
    }

}
