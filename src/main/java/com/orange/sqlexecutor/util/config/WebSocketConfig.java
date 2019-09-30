package com.orange.sqlexecutor.util.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * Created with IDEA
 * author:licheng
 * Date:2019/9/11
 * Time:上午12:08
 **/
@ConditionalOnWebApplication
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter () {
        return new ServerEndpointExporter();
    }
}
