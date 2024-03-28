package com.example.tgbotrusweb.bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TelegramBotModel {
    private String name;
    private String token;
}
