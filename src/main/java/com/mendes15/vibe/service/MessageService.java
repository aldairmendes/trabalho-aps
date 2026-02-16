package com.mendes15.vibe.service;

import com.mendes15.vibe.model.Message;
import com.mendes15.vibe.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    // Regra: Salvar uma mensagem
    public Message saveMessage(Message message) {
        // Aqui você poderia validar algo, ex: censurar palavras feias
        if (message.getContent() == null || message.getContent().isEmpty()) {
            throw new IllegalArgumentException("A mensagem não pode estar vazia");
        }
        return messageRepository.save(message);
    }

    // Regra: Listar o histórico do chat
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}