package com.vtb.guardian.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * API Security Guardian - Core Engine
 * 
 * Главный сервис для анализа безопасности API
 * 
 * @author Mojarung
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
@EnableFeignClients
public class CoreEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreEngineApplication.class, args);
    }

}

