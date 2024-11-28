package com.example.tgbotrusweb.logic.admin;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.tgbotrusweb.logic.UniqueChannelWordsHandler;
import com.example.tgbotrusweb.logic.domain.admin.AdminMessage;
import com.example.tgbotrusweb.logic.enums.AdminsChannels;
import com.example.tgbotrusweb.service.admin.TelegramResponseService;
import com.example.tgbotrusweb.service.admin.WordProcessingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WordHandlerTest {

  @Mock
  private WordProcessingService wordProcessingService;

  @Mock
  private TelegramResponseService telegramResponseService;

  @Mock
  private InputValidator inputValidator;

  @Mock
  private UniqueChannelWordsHandler uniqueChannelWordsHandler;

  @InjectMocks
  private WordHandler wordHandler;

  @Test
  void handleAddWordsCommand_ValidInput_Success() {
    // Arrange
    String inputText = "/add word1;word2;word3";
    AdminsChannels channel = AdminsChannels.ENGLISH;
    AdminMessage adminMessage = createMessage(inputText, channel.getId());
    String responseMessage = "Words added successfully";

    when(inputValidator.isValidWordsInput(inputText)).thenReturn(true);
    when(wordProcessingService.processAndSaveWords(inputText, channel.getId())).thenReturn(responseMessage);

    // Act
    wordHandler.handleCommand(adminMessage);

    // Assert
    verify(inputValidator).isValidWordsInput(inputText);
    verify(wordProcessingService).processAndSaveWords(inputText, channel.getId());
    verify(telegramResponseService).sendResponse(adminMessage, responseMessage);
  }

  @Test
  void handleShowWordsCommand_ValidInput_Success() {
    // Arrange
    String inputText = "/show";
    AdminsChannels channel = AdminsChannels.ENGLISH;
    AdminMessage adminMessage = createMessage(inputText, channel.getId());
    String responseMessage = "No words have been added yet.";

    when(inputValidator.isValidWordsInput(inputText)).thenReturn(true);
    when(wordProcessingService.getWordsForChannel(channel.getId())).thenReturn(responseMessage);

    // Act
    wordHandler.handleCommand(adminMessage);

    // Assert
    verify(inputValidator).isValidWordsInput(inputText);
    verify(wordProcessingService).getWordsForChannel(channel.getId());
    verify(telegramResponseService).sendResponse(adminMessage, responseMessage);
  }

  @Test
  void handleCommand_InvalidInput_ErrorResponse() {
    // Arrange
    String inputText = "invalid input text";
    AdminsChannels channel = AdminsChannels.ENGLISH;
    AdminMessage adminMessage = createMessage(inputText, channel.getId());
    String errorMessage = "Invalid input! Use '/add' to add words or '/show' to view the list of words. " +
        "For '/add', use words separated by ';'. Each word can contain letters, numbers, or underscores.";

    when(inputValidator.isValidWordsInput(inputText)).thenReturn(false);

    // Act
    wordHandler.handleCommand(adminMessage);

    // Assert
    verify(inputValidator).isValidWordsInput(inputText);
    verify(telegramResponseService).sendResponse(adminMessage, errorMessage);
    verify(wordProcessingService, never()).processAndSaveWords(anyString(), any());
  }

  private AdminMessage createMessage(String text, Integer messageThreadId) {
    return AdminMessage.builder().inputText(text).messageThreadId(messageThreadId).build();
  }
}
