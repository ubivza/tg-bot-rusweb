package com.example.tgbotrusweb.service.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

import com.example.tgbotrusweb.logic.enums.AdminsChannels;
import com.example.tgbotrusweb.logic.repository.WordFileRepository;
import java.util.Collections;
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
    Integer channel = AdminsChannels.ENGLISH.getId();

    Set<String> existingWords = Set.of("banana", "grape");
    Set<String> newWords = Set.of("apple", "cherry");
    Set<String> duplicateWords = Set.of("banana");

    when(wordFileRepository.readWordsFromFile(channel)).thenReturn(existingWords);
    when(wordFileRepository.saveNewWords(anyInt(), eq(newWords), eq(duplicateWords)))
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
    Integer channel = AdminsChannels.ENGLISH.getId();

    Set<String> existingWords = Set.of("banana", "grape");
    Set<String> newWords = Set.of("apple");
    Set<String> duplicateWords = new HashSet<>();

    when(wordFileRepository.readWordsFromFile(channel)).thenReturn(existingWords);
    when(wordFileRepository.saveNewWords(anyInt(), eq(newWords), eq(duplicateWords)))
        .thenReturn("Error saving words: Simulated error");

    // Act
    String result = wordProcessingService.processAndSaveWords(input, channel);

    // Assert
    assertEquals("Error saving words: Simulated error", result);
  }

  @Test
  void getWordsForChannel_Success() {
    // Arrange
    Integer channel = AdminsChannels.ENGLISH.getId();

    Set<String> existingWords = Set.of("banana", "grape");

    when(wordFileRepository.readWordsFromFile(channel)).thenReturn(existingWords);

    // Act
    String result = wordProcessingService.getWordsForChannel(channel);

    // Assert
    assertEquals("Words list: " + System.lineSeparator() + String.join(System.lineSeparator(), existingWords), result);
  }

  @Test
  void getWordsForChannel_EmptySet() {
    // Arrange
    Integer channel = AdminsChannels.ENGLISH.getId();

    Set<String> existingWords = Collections.emptySet();

    when(wordFileRepository.readWordsFromFile(channel)).thenReturn(existingWords);

    // Act
    String result = wordProcessingService.getWordsForChannel(channel);

    // Assert
    assertEquals("No words have been added yet.", result);
  }
}
