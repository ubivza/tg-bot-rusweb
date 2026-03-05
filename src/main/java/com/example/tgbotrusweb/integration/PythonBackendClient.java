package com.example.tgbotrusweb.integration;

import com.example.tgbotrusweb.exception.LLMRequestsLimitException;
import com.example.tgbotrusweb.integration.dto.CommentRequest;
import com.example.tgbotrusweb.integration.dto.CommentResponse;
import com.example.tgbotrusweb.logic.domain.Comment;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PythonBackendClient {
    private final HttpClient pythonClient;
    @Value("${python.backend.validation_endpoint}")
    private String validationPath;
    @Value("${python.backend.url}")
    private String pythonBackendUrl;
    private final ObjectMapper objectMapper;

    public Optional<String> validateComment(Comment comment) {
        try {
            String json = objectMapper.writeValueAsString(CommentRequest.builder().comment(comment.getUpdate().getMessage().getText()).build());

            HttpRequest validateRequest = HttpRequest.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1) // fast api в питоне не умеет http2, а клиент автоматом переключает
                    .timeout(Duration.ofSeconds(10))
                    .uri(URI.create(pythonBackendUrl + validationPath))
                    .header("Content-Type", "application/json; charset=utf-8")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> validationResponse = pythonClient.send(validateRequest, HttpResponse.BodyHandlers.ofString());

            if (validationResponse.statusCode() == 502) {
                log.error("LLM run out of requests or response is bad"); //TODO доработать
                sendToMe(comment, new Exception("Body: " + validationResponse.body() + "| Status: " + validationResponse.statusCode()));
                throw new LLMRequestsLimitException("LLM run out of requests or response is bad");
            }

            if (validationResponse.statusCode() >= 400) {
                log.error(validationResponse.body() + " " + validationResponse.statusCode());
                return Optional.empty();
            }

            CommentResponse commentResponse = objectMapper.readValue(validationResponse.body(), CommentResponse.class);

            if (commentResponse == null) {
                log.warn("Got null value from python backend");
                return Optional.empty();
            }

            return Optional.ofNullable(commentResponse.getIsSpam());
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendToMe(Comment comment, Exception exception) {
        SendMessage sendMessageRequest = new SendMessage("626688374", exception.getMessage());
        try {
            comment.getClient().execute(sendMessageRequest);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
