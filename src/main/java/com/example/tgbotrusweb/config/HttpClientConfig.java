package com.example.tgbotrusweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class HttpClientConfig {

    @Bean
    public HttpClient pythonClient() {
        return HttpClient.newBuilder()
                .build();
    }
}
