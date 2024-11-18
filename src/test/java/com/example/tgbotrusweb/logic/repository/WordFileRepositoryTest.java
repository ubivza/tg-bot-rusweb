package com.example.tgbotrusweb.logic.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WordFileRepositoryTest {

  @InjectMocks
  private WordFileRepository wordFileRepository;

  @Test
  void testReadWordsFromFile() {
    try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
      Stream<String> mockStream = Stream.of("word1", "word2", "word3");
      mockedFiles.when(() -> Files.lines(any(Path.class))).thenReturn(mockStream);

      Set<String> words = wordFileRepository.readWordsFromFile("testFile.txt");

      assertEquals(Set.of("word1", "word2", "word3"), words);
    }
  }

  @Test
  void testReadWordsFromFileEmpty() {
    try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
      mockedFiles.when(() -> Files.lines(any(Path.class))).thenReturn(Stream.empty());

      Set<String> words = wordFileRepository.readWordsFromFile("emptyFile.txt");

      assertTrue(words.isEmpty());
    }
  }

  @Test
  void testSaveNewWords() {
    try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
      BufferedWriter mockWriter = mock(BufferedWriter.class);
      mockedFiles.when(() -> Files.newBufferedWriter(any(Path.class), any())).thenReturn(mockWriter);

      Set<String> newWords = Set.of("word3");
      Set<String> duplicateWords = Set.of("word1");

      String result = wordFileRepository.saveNewWords("testFile.txt", newWords, duplicateWords);

      assertEquals("New words \"word3\" successfully added to file. These words already exist: word1", result);
    }
  }

  @Test
  void testSaveNewWordsNoNewWords() {
    Set<String> newWords = Set.of();
    Set<String> duplicateWords = Set.of("word1", "word2");

    String result = wordFileRepository.saveNewWords("fileName.txt", newWords, duplicateWords);

    assertTrue(result.startsWith("No new words to add."), "Expected message to start with 'No new words to add.'");
    assertTrue(result.contains("word1"), "Expected message to contain 'word1'");
    assertTrue(result.contains("word2"), "Expected message to contain 'word2'");

  }

}
