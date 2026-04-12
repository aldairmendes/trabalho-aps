package com.mendes15.vibe.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Cascata de fallback: Grok -> Gemini -> Claude -> Ollama (local).
 *
 * Cada client e opcional (@ConditionalOnBean no ChatClientConfig),
 * portanto beans ausentes nao impedem o boot — sao simplesmente pulados.
 */
@Service
public class AiService {

    // Mapa ordenado: preserva a ordem de fallback declarada
    private final Map<String, ChatClient> clients = new LinkedHashMap<>();

    public AiService(
            @Qualifier("grokClient")   Optional<ChatClient> grok,
            @Qualifier("geminiClient") Optional<ChatClient> gemini,
            @Qualifier("claudeClient") Optional<ChatClient> claude,
            @Qualifier("ollamaClient") Optional<ChatClient> ollama
    ) {
        grok   .ifPresent(c -> clients.put("Grok",   c));
        gemini .ifPresent(c -> clients.put("Gemini", c));
        claude .ifPresent(c -> clients.put("Claude", c));
        ollama .ifPresent(c -> clients.put("Ollama", c));

        if (clients.isEmpty()) {
            System.out.println("[AI] ATENCAO: nenhum provider de AI disponivel no contexto.");
        } else {
            System.out.println("[AI] Providers disponiveis: " + clients.keySet());
        }
    }

    /**
     * Tenta cada provider em ordem. Retorna a primeira resposta bem-sucedida.
     */
    public String ask(String userMessage) {
        for (var entry : clients.entrySet()) {
            try {
                System.out.println("[AI] Tentando " + entry.getKey() + "...");
                String response = entry.getValue()
                        .prompt()
                        .user(userMessage)
                        .call()
                        .content();
                System.out.println("[AI] Respondido por " + entry.getKey());
                return response;
            } catch (Exception e) {
                System.out.printf("[AI] %s falhou: %s%n", entry.getKey(), e.getMessage());
            }
        }
        return "Nenhum agente de IA disponivel no momento.";
    }

    /**
     * Forca um provider especifico sem fallback.
     * Util para endpoints que expõem a escolha ao usuario.
     */
    public String askWith(Provider provider, String userMessage) {
        var client = clients.get(provider.label);
        if (client == null) {
            throw new IllegalStateException("Provider " + provider.label + " nao esta disponivel.");
        }
        return client.prompt().user(userMessage).call().content();
    }

    public enum Provider {
        GROK("Grok"), GEMINI("Gemini"), CLAUDE("Claude"), OLLAMA("Ollama");

        final String label;
        Provider(String label) { this.label = label; }
    }
}