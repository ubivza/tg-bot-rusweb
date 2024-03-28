package com.example.tgbotrusweb.bot.config;

import com.example.tgbotrusweb.bot.TelegramBotGateway;
import com.example.tgbotrusweb.bot.model.TelegramBotModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@PropertySource("application.properties")
@Slf4j
public class TelegramBotConfiguration {
    @Value("${bot.name}")
    private String name;
    @Value("${bot.token}")
    private String token;


    @Bean
    public TelegramBotModel tgBot() {
        return TelegramBotModel.builder().name(name)
                .token(token).build();
    }

    @Bean
    public TelegramBotsApi tgBotConfigured(TelegramBotGateway gateway) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(gateway);
            return botsApi;
        } catch (TelegramApiException e) {
            log.warn(e + " creating new tg bot api");
            throw new RuntimeException("Couldnt create tg bot");
        }
    }
}
