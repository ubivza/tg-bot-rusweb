package com.example.tgbotrusweb.logic.admin;

import com.example.tgbotrusweb.logic.domain.Message;
import com.example.tgbotrusweb.logic.enums.AdminsChannels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class AddWordsInFile {

  private final AddWords addWords;

  private final InputValidator inputValidator;

  public AddWordsInFile(AddWords addWords, InputValidator inputValidator) {
    this.addWords = addWords;
    this.inputValidator = inputValidator;
  }

  /**
   * Метод для добавления слов в файл на основе сообщения.
   *
   * @param message Сообщение с текстом для добавления
   */
  public void addWordsInFile(Message message) {
    String inputText = message.getUpdate().getMessage().getText();
    AdminsChannels channel = message.getChannel();
    String responseMessage;

    if (inputValidator.isValidWordsInput(inputText)) {
      addWords.addWords(inputText, channel);
      responseMessage = String.format("Words \"%s\" added to the file.", inputText);
    } else {
      responseMessage =
          "Write one or more words separated by \";\". Each word can contain letters, numbers, or underscores.";
    }

    sendResponseMessage(message, responseMessage);
  }


  /**
   * Отправляет сообщение с ответом в чат.
   *
   * @param message Исходное сообщение
   * @param response Текст ответа
   */
  private void sendResponseMessage(Message message, String response) {
    SendMessage sendMessage = new SendMessage(
        message.getUpdate().getMessage().getChatId().toString(), response
    );
    sendMessage.setMessageThreadId(message.getUpdate().getMessage().getMessageThreadId());

    try {
      message.getClient().execute(sendMessage);
    } catch (TelegramApiException e) {
      System.err.println("Failed to send message: " + e.getMessage());
      throw new RuntimeException("Error sending Telegram message", e);
    }
  }
}
