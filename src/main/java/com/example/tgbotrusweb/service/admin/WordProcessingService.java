package com.example.tgbotrusweb.service.admin;

import com.example.tgbotrusweb.logic.enums.AdminsChannels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WordProcessingService {

  public String processAndSaveWords(String input, AdminsChannels channel) {
    Set<String> inputWords = parseWords(input);
    String fileName = resolveFileName(channel);

    Set<String> existingWords = readWordsFromFile(fileName);
    Set<String> duplicateWords = findDuplicates(inputWords, existingWords);
    Set<String> newWords = findNewWords(inputWords, existingWords);

    logDuplicates(duplicateWords);

    return saveNewWords(fileName, newWords, duplicateWords);
  }

  private Set<String> parseWords(String input) {
    return new HashSet<>(Arrays.asList(input.split(";\\s*")));
  }

  private String resolveFileName(AdminsChannels channel) {
    return switch (channel) {
      case ITALY -> "src/main/resources/words/italian_words";
      case FRENCH -> "src/main/resources/words/french_words";
      case GERMAN -> "src/main/resources/words/german_words";
      case ENGLISH -> "src/main/resources/words/english_words";
    };
  }

  private Set<String> readWordsFromFile(String fileName) {
    try {
      return Files.lines(Paths.get(fileName))
          .map(String::trim)
          .filter(line -> !line.isEmpty())
          .collect(Collectors.toSet());
    } catch (IOException e) {
      log.error("Error reading from file: {}", e.getMessage());
      return Collections.emptySet();
    }
  }

  private Set<String> findDuplicates(Set<String> inputWords, Set<String> existingWords) {
    Set<String> duplicates = new HashSet<>(inputWords);
    duplicates.retainAll(existingWords);
    return duplicates;
  }

  private Set<String> findNewWords(Set<String> inputWords, Set<String> existingWords) {
    Set<String> newWords = new HashSet<>(inputWords);
    newWords.removeAll(existingWords);
    return newWords;
  }

  private void logDuplicates(Set<String> duplicateWords) {
    if (!duplicateWords.isEmpty()) {
      log.info("These words already exist: {}", String.join(", ", duplicateWords));
    }
  }

  private String saveNewWords(String fileName, Set<String> newWords, Set<String> duplicateWords) {
    if (newWords.isEmpty()) {
      return "No new words to add. These words already exist: " + String.join(", ", duplicateWords);
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
      for (String word : newWords) {
        writer.write(word);
        writer.newLine();
      }
      log.info("New words added: {}", String.join(", ", newWords));
      return "New words \"" + String.join(", ", newWords) + "\" successfully added to file. " +
          (duplicateWords.isEmpty() ? "" : "These words already exist: " + String.join(", ", duplicateWords));
    } catch (IOException e) {
      log.error("File write error: {}", e.getMessage());
      return "Error saving words: " + e.getMessage();
    }
  }
}
