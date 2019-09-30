package com.orange.sqlexecutor.util.eventbus.eventbusConf;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

/**
 * Created with IDEA
 * author:licheng
 * Date:2019/9/5
 * Time:下午8:06
 **/
@Configuration
public class EventBusConfBean {

    @Bean
    public AsyncEventBus asyncEventBus(){
        return new AsyncEventBus(Executors.newFixedThreadPool(50));
    }
}
