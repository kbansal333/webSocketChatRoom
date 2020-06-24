package com.example.chatRoom.controller;

import com.example.chatRoom.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @MessageMapping("/user")
    @SendTo("/topic/user")
    public Message getMessage(){return null;}
}
