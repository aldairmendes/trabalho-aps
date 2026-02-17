package com.mendes15.vibe.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = "vibe-messages")
    public void receiveMessage(String message) {
        // Imprime no console como você já faz
        System.out.println("Enviando para WebSocket: " + message);
        
        // Empurra a mensagem para todos os inscritos no tópico "/topic/messages"
        messagingTemplate.convertAndSend("/topic/messages", message);
    }
}