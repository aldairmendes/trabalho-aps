package com.mendes15.vibe.service;

import com.mendes15.vibe.model.Message;
import com.mendes15.vibe.repository.MessageRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Message saveMessage(Message message) {
        // 1. Salva no Postgres
        Message savedMessage = messageRepository.save(message);
        
        // 2. Envia para a fila do RabbitMQ (usando o nome da fila 'vibe-messages')
        rabbitTemplate.convertAndSend("vibe-messages", savedMessage.getContent());
        
        return savedMessage;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}