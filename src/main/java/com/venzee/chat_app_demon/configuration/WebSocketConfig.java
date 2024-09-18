package com.venzee.chat_app_demon.configuration;

import com.venzee.chat_app_demon.utility.TutorialHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

import java.util.List;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
   public void registerStompEndpoints(StompEndpointRegistry registry) {
       registry.addEndpoint("/spring-boot-tutorial");
    }


    public void configureMessageBroker(MessageBrokerRegistry config) {
       config.setApplicationDestinationPrefixes("/app");
       config.enableSimpleBroker("/topic");
    }

}