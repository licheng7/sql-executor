package com.orange.sqlexecutor.util.eventbus;

import org.springframework.beans.factory.InitializingBean;

/**
 * Created with IDEA
 * author:licheng
 * Date:2019/9/5
 * Time:下午8:11
 **/
public interface EventBusListener<T extends EventInfo> extends InitializingBean {

    void listener(T info);

    void registry();

    default void afterPropertiesSet() throws Exception {
        this.registry();
    }
}
