package com.pro.sell.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


@Component
public class WebSocketConfig {

    //使用SpringBootTest时，需要先将此bean注释
    //因为ServerEndpointExporter Bean的创建需要一个真实的Tomcat环境
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
