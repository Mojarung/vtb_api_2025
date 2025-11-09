package com.vtb.guardian.llm.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vtb.guardian.llm.client.LlmClient;
import com.vtb.guardian.llm.config.LlmConfig;
import com.vtb.guardian.llm.model.LlmAnalysisResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Nvidia API LLM Client
 * Uses Qwen/qwen3-next-80b-a3b-instruct model
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NvidiaLlmClient implements LlmClient {
    
    private static final String NVIDIA_API_URL = "https://integrate.api.nvidia.com/v1/chat/completions";
    private static final String MODEL = "qwen/qwen3-next-80b-a3b-instruct";
    
    private final LlmConfig config;
    private final ObjectMapper objectMapper;
    
    private final OkHttpClient httpClient = new OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build();
    
    @Override
    @Cacheable(value = "llm-analysis", key = "#userPrompt.hashCode()")
    public LlmAnalysisResult analyze(String systemPrompt, String userPrompt) {
        log.info("Calling Nvidia API (Qwen model) for security analysis");
        
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", MODEL);
            requestBody.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userPrompt)
            ));
            requestBody.put("temperature", 0.2);
            requestBody.put("top_p", 0.7);
            requestBody.put("max_tokens", 4096);
            requestBody.put("stream", false);
            
            RequestBody body = RequestBody.create(
                objectMapper.writeValueAsString(requestBody),
                MediaType.parse("application/json")
            );
            
            Request request = new Request.Builder()
                .url(NVIDIA_API_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + config.getNvidiaApiKey())
                .post(body)
                .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Nvidia API error: " + response);
                }
                
                String responseBody = response.body().string();
                Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
                
                // Extract content from response
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                String content = (String) message.get("content");
                
                // Parse JSON content to LlmAnalysisResult
                return parseAnalysisResult(content);
            }
            
        } catch (Exception e) {
            log.error("Nvidia API call failed", e);
            throw new RuntimeException("Failed to analyze with Nvidia API", e);
        }
    }
    
    @Override
    public String generateText(String prompt, int maxTokens) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", MODEL);
            requestBody.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
            ));
            requestBody.put("temperature", 0.7);
            requestBody.put("top_p", 0.7);
            requestBody.put("max_tokens", maxTokens);
            requestBody.put("stream", false);
            
            RequestBody body = RequestBody.create(
                objectMapper.writeValueAsString(requestBody),
                MediaType.parse("application/json")
            );
            
            Request request = new Request.Builder()
                .url(NVIDIA_API_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + config.getNvidiaApiKey())
                .post(body)
                .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Nvidia API error: " + response);
                }
                
                String responseBody = response.body().string();
                Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
                
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                return (String) message.get("content");
            }
            
        } catch (Exception e) {
            log.error("Nvidia API text generation failed", e);
            throw new RuntimeException("Failed to generate text with Nvidia API", e);
        }
    }
    
    @Override
    public <T> T generateStructured(String prompt, Class<T> responseType) {
        String json = generateText(prompt + "\n\nRespond with valid JSON only.", 4096);
        try {
            return objectMapper.readValue(json, responseType);
        } catch (Exception e) {
            log.error("Failed to parse structured response", e);
            throw new RuntimeException("Failed to parse JSON response", e);
        }
    }
    
    private LlmAnalysisResult parseAnalysisResult(String jsonContent) {
        try {
            return objectMapper.readValue(jsonContent, LlmAnalysisResult.class);
        } catch (Exception e) {
            log.error("Failed to parse analysis result", e);
            throw new RuntimeException("Failed to parse LLM analysis result", e);
        }
    }
    
    @Override
    public String getName() {
        return "nvidia";
    }
}

