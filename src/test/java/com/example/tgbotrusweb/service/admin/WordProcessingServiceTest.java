package com.example.tgbotrusweb.service.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

import com.example.tgbotrusweb.logic.enums.AdminsChannels;
import com.example.tgbotrusweb.logic.repository.WordFileRepository;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WordProcessingServiceTest {

  @Mock
  private WordFileRepository wordFileRepository;

  @InjectMocks
  private WordProcessingService wordProcessingService;

  @Test
  void processAndSaveWords_NewAndDuplicateWords_Success() {
    // Arrange
    String input = "/add apple;banana;cherry;apple";
    AdminsChannels channel = AdminsChannels.ENGLISH;

    Set<String> existingWords = Set.of("banana", "grape");
    Set<String> newWords = Set.of("apple", "cherry");
    Set<String> duplicateWords = Set.of("banana");

    when(wordFileRepository.readWordsFromFile(anyString())).thenReturn(existingWords);
    when(wordFileRepository.saveNewWords(anyString(), eq(newWords), eq(duplicateWords)))
        .thenReturn("New words \"apple, cherry\" successfully added to file. These words already exist: banana.");

    // Act
    String result = wordProcessingService.processAndSaveWords(input, channel);

    // Assert
    assertEquals("New words \"apple, cherry\" successfully added to file. These words already exist: banana.", result);
  }

  @Test
  void processAndSaveWords_FileWriteError_ErrorMessage() {
    // Arrange
    String input = "/add apple";
    AdminsChannels channel = AdminsChannels.ENGLISH;

    Set<String> existingWords = Set.of("banana", "grape");
    Set<String> newWords = Set.of("apple");
    Set<String> duplicateWords = new HashSet<>();

    when(wordFileRepository.readWordsFromFile(anyString())).thenReturn(existingWords);
    when(wordFileRepository.saveNewWords(anyString(), eq(newWords), eq(duplicateWords)))
        .thenReturn("Error saving words: Simulated error");

    // Act
    String result = wordProcessingService.processAndSaveWords(input, channel);

    // Assert
    assertEquals("Error saving words: Simulated error", result);
  }
}
