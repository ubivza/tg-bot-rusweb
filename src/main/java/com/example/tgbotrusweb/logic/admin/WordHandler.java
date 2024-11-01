package com.example.tgbotrusweb.logic.admin;

import com.example.tgbotrusweb.logic.domain.admin.AdminMessage;
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
   * @param adminMessage Сообщение от пользователя.
   */
  public void handleAddWordsCommand(AdminMessage adminMessage) {
    String inputText = adminMessage.getUpdate().getMessage().getText();
    AdminsChannels channel = adminMessage.getChannel();

    if (!inputValidator.isValidWordsInput(inputText)) {
      String errorMessage = "Invalid input! The input should start with '/add'. " +
          "Use words separated by ';'. Each word can contain letters, numbers, or underscores.";
      telegramResponseService.sendResponse(adminMessage, errorMessage);
      log.info(errorMessage);
      return;
    }

    String responseMessage = wordProcessingService.processAndSaveWords(inputText, channel);
    telegramResponseService.sendResponse(adminMessage, responseMessage);
  }
}
