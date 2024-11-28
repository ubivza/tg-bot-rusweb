package com.example.tgbotrusweb.logic.admin;

import com.example.tgbotrusweb.logic.GeneralWordsHandler;
import com.example.tgbotrusweb.logic.domain.admin.AdminMessage;
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
  private final GeneralWordsHandler generalWordsHandler;
  private static final String ERROR_MESSAGE = "Invalid input! Use '/add' to add words or '/show' to view the list of words. " +
      "For '/add', use words separated by ';'. Each word can contain letters, numbers, or underscores.";


  public void handleCommand(AdminMessage adminMessage) {
    String inputText = adminMessage.getInputText();
    Integer messageThreadId = adminMessage.getMessageThreadId();

    if (!inputValidator.isValidWordsInput(inputText)) {
      telegramResponseService.sendResponse(adminMessage, ERROR_MESSAGE);
      log.info(ERROR_MESSAGE);
      return;
    }
    if (inputText.startsWith("/add")) {
      handleAddWordsCommand(adminMessage, inputText, messageThreadId);
    } else if (inputText.startsWith("/show")) {
      handleShowWordsCommand(adminMessage, messageThreadId);
    }
  }

  /**
   * Обрабатывает команду добавления слов.
   *
   * @param adminMessage Сообщение от пользователя.
   */
  private void handleAddWordsCommand(AdminMessage adminMessage, String inputText, Integer messageThreadId) {
    String responseMessage = wordProcessingService.processAndSaveWords(inputText, messageThreadId);
    telegramResponseService.sendResponse(adminMessage, responseMessage);
  }

  /**
   * Обрабатывает команду отображения списка слов.
   *
   * @param adminMessage Сообщение от пользователя.
   */
  public void handleShowWordsCommand(AdminMessage adminMessage, Integer messageThreadId) {
    String responseMessage = wordProcessingService.getWordsForChannel(messageThreadId);

    telegramResponseService.sendResponse(adminMessage, responseMessage);
  }

}
