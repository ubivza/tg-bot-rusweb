package com.example.tgbotrusweb.logic.domain;

import com.example.tgbotrusweb.logic.enums.AdminsChannels;
import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Data
@Builder
public class Message {

  private Update update;
  private TelegramClient client;
  private AdminsChannels channel;
}
