package com.mendes15.vibe.consumer;

import com.mendes15.vibe.service.AiService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private AiService aiService;

    @RabbitListener(queues = "vibe-messages")
    public void receiveMessage(String message) {
        System.out.println("Mensagem recebida do RabbitMQ: " + message);

        String aiResponse = aiService.ask(message);

        System.out.println("Resposta da IA: " + aiResponse);

        messagingTemplate.convertAndSend("/topic/messages", aiResponse);
    }
}