package com.example.tgbotrusweb.service.admin;

import com.example.tgbotrusweb.logic.domain.admin.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
public class TelegramResponseService {

  public void sendResponse(Message message, String response) {
    SendMessage sendMessage = new SendMessage(
        message.getUpdate().getMessage().getChatId().toString(), response
    );
    sendMessage.setMessageThreadId(message.getUpdate().getMessage().getMessageThreadId());

    try {
      message.getClient().execute(sendMessage);
    } catch (TelegramApiException e) {
      log.error("Failed to send message: {}", e.getMessage());
      throw new RuntimeException("Error sending Telegram message", e);
    }
  }
}
