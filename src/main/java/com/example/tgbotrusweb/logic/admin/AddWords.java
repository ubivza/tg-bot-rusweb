package com.example.tgbotrusweb.logic.admin;

import com.example.tgbotrusweb.logic.enums.AdminsChannels;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AddWords {

  public void addWords(String words, AdminsChannels channel) {
    Set<String> uniqueWords = new HashSet<>(Arrays.asList(words.split(";\\s*")));
    String outputFileName = switch (channel) {
      case ITALY -> "src/main/resources/words/italian_words";
      case FRENCH -> "src/main/resources/words/french_words";
      case GERMAN -> "src/main/resources/words/german_words";
      case ENGLISH -> "src/main/resources/words/english_words";
    };

    Set<String> existingWords = readWordsFromFile(outputFileName);

    Set<String> duplicateWords = new HashSet<>(uniqueWords);
    duplicateWords.retainAll(existingWords);

    Set<String> newWords = new HashSet<>(uniqueWords);
    newWords.removeAll(existingWords);

    if (!duplicateWords.isEmpty()) {
      log.info("These words already exist: " + String.join(", ", duplicateWords));
    }

    // Добавляем только новые слова в файл
    if (!newWords.isEmpty()) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName, true))) {
        for (String word : newWords) {
          writer.write(word);
          writer.newLine();
        }
        log.info("New words \"" + String.join(", ", newWords) + "\" successfully added to file: " + outputFileName);
      } catch (IOException e) {
        log.info("File record error: " + e.getMessage());
      }
    } else {
      log.info("No new words to add.");
    }

  }

  // Метод для чтения слов из файла
  private Set<String> readWordsFromFile(String fileName) {
    try {
      return Files.lines(Paths.get(fileName))
          .map(String::trim)
          .filter(line -> !line.isEmpty())
          .collect(Collectors.toSet());
    } catch (IOException e) {
      log.info("Error reading from file: " + e.getMessage());
      return Collections.emptySet();
    }
  }

}
