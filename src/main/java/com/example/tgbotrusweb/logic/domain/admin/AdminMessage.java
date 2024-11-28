package com.example.tgbotrusweb.logic.domain.admin;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Data
@Builder
public class AdminMessage {

  private String inputText;
  private Long chatId;
  private Integer messageThreadId;
  private TelegramClient client;

}
