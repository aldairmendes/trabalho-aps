package com.mendes15.vibe.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.mendes15.vibe.model.Message;
import com.mendes15.vibe.repository.MessageRepository;

@Service
public class AIService {

    private final ChatClient chatClient;
    private final MessageRepository messageRepository;

    // O Spring Boot injeta o ChatClient.Builder automaticamente com as configs do YAML
    public AIService(ChatClient.Builder builder, MessageRepository messageRepository) {
        this.chatClient = builder.build();
        this.messageRepository = messageRepository;
    }

    // Gera uma resposta da IA baseada no contexto das últimas mensagens da sala.
    public String gerarRespostaComContexto(String roomId) {
        List<Message> historico = messageRepository.findTop10ByRoomIdOrderByTimestampDesc(roomId);
        
        // Invertemos a lista para que a ordem fique cronológica (Antiga -> Nova)
        Collections.reverse(historico);

        // FORMATAÇÃO E PROTEÇÃO: Transforma a lista em texto e evita "mensagens bíblicas"
        String contexto = historico.stream()
                .map(m -> {
                    String conteudo = m.getContent();
                    if (conteudo != null && conteudo.length() > 500) {
                        conteudo = conteudo.substring(0, 500) + "... [Texto muito longo para o contexto]";
                    }
                    return m.getSender() + ": " + conteudo;
                })
                .collect(Collectors.joining("\n"));

        // GERAÇÃO: Monta o prompt e envia para o Gemini
        return chatClient.prompt()
                .system("Você é o Vibe-Assistant, um assistente inteligente integrado a um chat acadêmico. " +
                        "Sua tarefa é analisar o histórico de mensagens fornecido e responder de forma útil, " +
                        "curta e direta. Se não houver mensagens suficientes, peça para o pessoal conversar mais!")
                .user("Aqui está o histórico recente da conversa:\n\n" + contexto + 
                      "\n\nCom base nessas mensagens, faça um resumo ou responda à última interação.")
                .call()
                .content();
    }
}