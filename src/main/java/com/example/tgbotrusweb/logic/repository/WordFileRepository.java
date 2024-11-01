package com.example.tgbotrusweb.logic.repository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WordFileRepository {

  public Set<String> readWordsFromFile(String fileName) {
    try (var lines = Files.lines(Paths.get(fileName))) {
      return lines.map(String::trim)
          .filter(line -> !line.isEmpty())
          .collect(Collectors.toSet());
    } catch (IOException e) {
      return Collections.emptySet();
    }
  }

  public String saveNewWords(String fileName, Set<String> newWords, Set<String> duplicateWords) {
    if (newWords.isEmpty()) {
      String message = "No new words to add. These words already exist: " + String.join(", ", duplicateWords);
      log.info(message);
      return message;
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
