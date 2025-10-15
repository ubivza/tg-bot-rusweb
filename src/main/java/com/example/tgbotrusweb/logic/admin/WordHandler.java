package com.example.tgbotrusweb.logic.admin;

import com.example.tgbotrusweb.logic.GeneralWordsHandler;
import com.example.tgbotrusweb.logic.UniqueChannelWordsHandler;
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
  private final UniqueChannelWordsHandler uniqueChannelWordsHandler;
  private final GeneralWordsHandler generalWordsHandler;
  private static final String ERROR_MESSAGE = "Invalid input! Use '/add' to add words or '/show' to view the list of words. " +
      "For '/add', use words separated by ';'. Each word can contain letters, numbers, or underscores.";


  public void handleCommand(AdminMessage adminMessage) {
    String inputText = adminMessage.getUpdate().getMessage().getText();
    AdminsChannels channel = adminMessage.getChannel();

    if (!inputValidator.isValidWordsInput(inputText)) {
      telegramResponseService.sendResponse(adminMessage, ERROR_MESSAGE);
      log.info(ERROR_MESSAGE);
      return;
    }
    if (inputText.startsWith("/add")) {
      handleAddWordsCommand(adminMessage, inputText, channel);
    } else if (inputText.startsWith("/show")) {
      handleShowWordsCommand(adminMessage, channel);
    } else {
      telegramResponseService.sendResponse(adminMessage, ERROR_MESSAGE);
      log.info(ERROR_MESSAGE);
    }
  }

  /**
   * Обрабатывает команду добавления слов.
   *
   * @param adminMessage Сообщение от пользователя.
   */
  private void handleAddWordsCommand(AdminMessage adminMessage, String inputText, AdminsChannels channel) {
    String responseMessage = wordProcessingService.processAndSaveWords(inputText, channel);
    updateActualWordList(channel);
    telegramResponseService.sendResponse(adminMessage, responseMessage);
  }

  /**
   * Обрабатывает команду отображения списка слов.
   *
   * @param adminMessage Сообщение от пользователя.
   */
  public void handleShowWordsCommand(AdminMessage adminMessage, AdminsChannels channel) {
    String responseMessage = wordProcessingService.getWordsForChannel(channel);

    telegramResponseService.sendResponse(adminMessage, responseMessage);
  }

  private void updateActualWordList(AdminsChannels channels) {
    switch (channels) {
      case ENGLISH -> uniqueChannelWordsHandler.updateEnglishWords();
      case FRENCH -> uniqueChannelWordsHandler.updateFrenchWords();
      case ITALY -> uniqueChannelWordsHandler.updateItalianWords();
      case GERMAN -> uniqueChannelWordsHandler.updateGermanWords();
      case GENERAL -> generalWordsHandler.updateWords();
      case SPANISH -> uniqueChannelWordsHandler.updateSpanishWords();
      case RUSSIAN -> uniqueChannelWordsHandler.updateRussianWords();
    }
  }
}
