package com.orange.sqlexecutor.util.eventbus;

/**
 * Created with IDEA
 * author:licheng
 * Date:2019/9/5
 * Time:下午7:34
 **/
public abstract class EventInfo {

    private String eventName;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
