package com.mendes15.vibe.controller;

import com.mendes15.vibe.model.Message;
import com.mendes15.vibe.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    // Endpoint para enviar uma nova mensagem
    @PostMapping
    public Message send(@RequestBody Message message) {
        return messageService.saveMessage(message);
    }

    // Endpoint para listar todas as mensagens do banco
    @GetMapping
    public List<Message> list() {
        return messageService.getAllMessages();
    }
}