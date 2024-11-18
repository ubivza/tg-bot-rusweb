package com.example.tgbotrusweb.logic.admin;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.tgbotrusweb.logic.domain.admin.AdminMessage;
import com.example.tgbotrusweb.logic.enums.AdminsChannels;
import com.example.tgbotrusweb.service.admin.TelegramResponseService;
import com.example.tgbotrusweb.service.admin.WordProcessingService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Update;

@ExtendWith(MockitoExtension.class)
@Disabled
class WordHandlerTest {

  @Mock
  private WordProcessingService wordProcessingService;

  @Mock
  private TelegramResponseService telegramResponseService;

  @Mock
  private InputValidator inputValidator;

  @InjectMocks
  private WordHandler wordHandler;

  @Test
  void handleAddWordsCommand_ValidInput_Success() {
    // Arrange
    String inputText = "/add word1;word2;word3";
    AdminsChannels channel = AdminsChannels.ENGLISH;
    AdminMessage adminMessage = createMessage(inputText, channel);
    String responseMessage = "Words added successfully";

    when(inputValidator.isValidWordsInput(inputText)).thenReturn(true);
    when(wordProcessingService.processAndSaveWords(inputText, channel)).thenReturn(responseMessage);

    // Act
    wordHandler.handleAddWordsCommand(adminMessage);

    // Assert
    verify(inputValidator).isValidWordsInput(inputText);
    verify(wordProcessingService).processAndSaveWords(inputText, channel);
    verify(telegramResponseService).sendResponse(adminMessage, responseMessage);
  }

  @Test
  void handleAddWordsCommand_InvalidInput_ErrorResponse() {
    // Arrange
    String inputText = "invalid input text";
    AdminsChannels channel = AdminsChannels.ENGLISH;
    AdminMessage adminMessage = createMessage(inputText, channel);
    String errorMessage = "Invalid input! The input should start with '/add'. " +
        "Use words separated by ';'. Each word can contain letters, numbers, or underscores.";

    when(inputValidator.isValidWordsInput(inputText)).thenReturn(false);

    // Act
    wordHandler.handleAddWordsCommand(adminMessage);

    // Assert
    verify(inputValidator).isValidWordsInput(inputText);
    verify(telegramResponseService).sendResponse(adminMessage, errorMessage);
    verify(wordProcessingService, never()).processAndSaveWords(anyString(), any());
  }

  private AdminMessage createMessage(String text, AdminsChannels channel) {
    var ms = new org.telegram.telegrambots.meta.api.objects.message.Message();
    ms.setText(text);
    var update = new Update();
    update.setMessage(ms);

    return AdminMessage.builder().update(update).channel(channel).build();
  }
}
