package com.example.tgbotrusweb.logic.admin;

import com.example.tgbotrusweb.logic.domain.admin.InputValidator;
import com.example.tgbotrusweb.logic.domain.admin.Message;
import com.example.tgbotrusweb.logic.enums.AdminsChannels;
import com.example.tgbotrusweb.service.admin.TelegramResponseService;
import com.example.tgbotrusweb.service.admin.WordProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@RequiredArgsConstructor
public class WordHandler {

  private final WordProcessingService wordProcessingService;
  private final TelegramResponseService telegramResponseService;
  private final InputValidator inputValidator;

  /**
   * Обрабатывает команду добавления слов.
   *
   * @param message Сообщение от пользователя.
   */
  public void handleAddWordsCommand(Message message) {
    String inputText = message.getUpdate().getMessage().getText();
    AdminsChannels channel = message.getChannel();

    if (!inputValidator.isValidWordsInput(inputText)) {
      String errorMessage = "Invalid input! Use words separated by ';'. " +
          "Each word can contain letters, numbers, or underscores.";
      telegramResponseService.sendResponse(message, errorMessage);
      log.info(errorMessage);
      return;
    }

    String responseMessage = wordProcessingService.processAndSaveWords(inputText, channel);
    telegramResponseService.sendResponse(message, responseMessage);
  }
}
