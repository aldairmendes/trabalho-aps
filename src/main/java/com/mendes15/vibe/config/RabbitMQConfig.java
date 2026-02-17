package com.mendes15.vibe.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue messageQueue() {
        // Isso garante que a fila 'vibe-messages' exista assim que o app subir
        return new Queue("vibe-messages", true);
    }
}