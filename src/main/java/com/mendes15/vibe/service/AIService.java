package com.mendes15.vibe.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import com.mendes15.vibe.repository.MessageRepository;

@Service
public class AIService {

    private final ChatModel chatModel;
    private final MessageRepository messageRepository;

    // Na M1-PLATFORM, injetamos o ChatModel diretamente, pois o Builder é do ChatClient
    public AIService(ChatModel chatModel, MessageRepository messageRepository) {
        this.chatModel = chatModel;
        this.messageRepository = messageRepository;
    }

    public String gerarRespostaComContexto(String roomId) {
        // Buscamos o histórico (supondo que seu repository use o model com M maiúsculo)
        List<com.mendes15.vibe.model.Message> historico = messageRepository.findTop10ByRoomIdOrderByTimestampDesc(roomId);
        
        Collections.reverse(historico);

        String contextoTexto = historico.stream()
                .map(m -> {
                    String conteudo = m.getContent();
                    if (conteudo != null && conteudo.length() > 500) {
                        conteudo = conteudo.substring(0, 500) + "...";
                    }
                    return m.getSender() + ": " + conteudo;
                })
                .collect(Collectors.joining("\n"));

        // --- ADAPTAÇÃO PARA CHATMODEL ---
        
        // 1. Criamos a mensagem de sistema (Instruções)
        SystemMessage systemMessage = new SystemMessage(
            "Você é o Vibe-Assistant, um assistente inteligente integrado a um chat acadêmico. " +
            "Sua tarefa é analisar o histórico e responder de forma curta e direta."
        );

        // 2. Criamos a mensagem do usuário com o contexto
        UserMessage userMessage = new UserMessage(
            "Aqui está o histórico recente da conversa:\n\n" + contextoTexto + 
            "\n\nCom base nessas mensagens, faça um resumo ou responda à última interação."
        );

        // 3. Montamos o Prompt com a lista de mensagens
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        // 4. Chamamos o modelo e pegamos o conteúdo do primeiro resultado
        return chatModel.call(prompt).getResult().getOutput().getText();
    }
}