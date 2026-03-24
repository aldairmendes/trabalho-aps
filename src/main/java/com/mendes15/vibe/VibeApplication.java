package com.mendes15.vibe;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.mendes15.vibe.service.AIService;

@SpringBootApplication
public class VibeApplication {

	public static void main(String[] args) {
		SpringApplication.run(VibeApplication.class, args);
	}
	
    
}
