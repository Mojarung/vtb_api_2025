package com.vtb.guardian.llm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * LLM Service - AI-powered vulnerability analysis
 * 
 * Uses Nvidia API with Qwen model for intelligent security analysis
 * 
 * @author Mojarung
 * @version 1.0.0
 */
@SpringBootApplication
@EnableCaching
public class LlmServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LlmServiceApplication.class, args);
    }

}

