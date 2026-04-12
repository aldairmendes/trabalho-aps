package com.mendes15.vibe.config;

import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ChatClientConfig {

    /**
     * Grok — usa o starter OpenAI apontado para api.x.ai.
     * @Primary garante que injecoes sem @Qualifier usem este.
     * So criado se o OpenAiChatModel foi autoconfigurado (api-key presente).
     */
    @Bean
    @Primary
    @ConditionalOnBean(OpenAiChatModel.class)
    public ChatClient grokClient(OpenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("You are a helpful assistant powered by Grok (xAI).")
                .build();
    }

    /**
     * Google Gemini.
     * So criado se o GoogleGenAiChatModel foi autoconfigurado.
     */
    @Bean
    @ConditionalOnBean(GoogleGenAiChatModel.class)
    public ChatClient geminiClient(GoogleGenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("You are a helpful assistant powered by Google Gemini.")
                .build();
    }

    /**
     * Anthropic Claude.
     * So criado se o AnthropicChatModel foi autoconfigurado.
     */
    @Bean
    @ConditionalOnBean(AnthropicChatModel.class)
    public ChatClient claudeClient(AnthropicChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("You are a helpful assistant powered by Anthropic Claude.")
                .build();
    }

    /**
     * Ollama - LLM local via Docker.
     * So criado se o OllamaChatModel foi autoconfigurado
     * (exige spring-ai-starter-model-ollama no classpath E container rodando).
     */
    @Bean
    @ConditionalOnBean(OllamaChatModel.class)
    public ChatClient ollamaClient(OllamaChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("You are a helpful assistant running locally via Ollama.")
                .build();
    }
}