package com.venzee.chat_app_demon.controller;

import com.venzee.chat_app_demon.utility.MemberStore;
import com.venzee.chat_app_demon.utility.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.lang.reflect.Member;
import java.time.Instant;
import java.util.Map;

@Slf4j
public class ChatController {
    @Autowired
    private MemberStore memberStore;
    @Autowired
    private  SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/user")
    public void getUsers(User user, SimpMessageHeaderAccessor headerAccessor){
        User newUser = new User(user.id(), user.username());
        headerAccessor.getSessionAttributes().put("user",newUser);
        memberStore.addMember(newUser);
        if(!memberStore.getMembers().isEmpty()){
            simpMessagingTemplate.convertAndSend("/topic/users",memberStore.getMembers());
        }
        Message newMessage = new Message(newUser,null,Action.JOINED, Instant.now());
        simpMessagingTemplate.convertAndSend("/topic/messages",newMessage);
    }

    @EventListener
    public void handleSessionConnectEvent(SessionConnectedEvent event){
        log.info("Session Connect Event");
    }
    @EventListener
    public void handleSessionDisconnectEvent(SessionDisconnectEvent event){
        log.info("Session Disconnect Event");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        if (sessionAttributes == null){
            return;
        }
        User user = (User) sessionAttributes.get("user");
        if (user==null){
            return;
        }
        memberStore.removeMember(user);
    }

}
