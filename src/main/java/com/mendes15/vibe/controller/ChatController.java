package com.mendes15.vibe.controller;

import com.mendes15.vibe.model.Message;
import com.mendes15.vibe.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller // Use @Controller para WebSockets, não @RestController
public class ChatController {

    @Autowired
    private MessageService messageService;

    // Esse método recebe o que o React envia para "/app/chat"
    @MessageMapping("/chat")
    // Esse método envia o resultado para todos que ouvem "/topic/messages"
    @SendTo("/topic/messages")
    public Message processMessageFromClient(Message message) {
        // Salva no banco de dados para não perder o histórico
        return messageService.saveMessage(message);
    }
}