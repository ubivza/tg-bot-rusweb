package com.example.tgbotrusweb.logic.repository;

import java.io.IOException;
import java.io.RandomAccessFile;
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
          .map(String::toLowerCase)
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

    return save(fileName, newWords, duplicateWords);
  }

  private static String save(String fileName, Set<String> newWords, Set<String> duplicateWords) {
    try (RandomAccessFile file = new RandomAccessFile(fileName, "rw")) {
      long fileLength = file.length();
      boolean isNewLineNeeded = true;

      if (fileLength > 0) {
        // Move the cursor to the last character
        file.seek(fileLength - 1);
        char lastChar = (char) file.readByte();

        // Check if the last character is a newline
        isNewLineNeeded = lastChar != '\n';
      }

      // Move the cursor to the end of the file
      file.seek(fileLength);

      // Write a newline if needed
      if (isNewLineNeeded) {
        file.write(System.lineSeparator().getBytes());
      }

      // Write new content
      for (String word : newWords) {
        file.write(word.getBytes());
        file.write(System.lineSeparator().getBytes());
      }
      log.info("New words added: {}", String.join(", ", newWords));
      return "New words \"" + String.join(", ", newWords) + "\" successfully added to file. " +
          (duplicateWords.isEmpty() ? "" : "These words already exist: " + String.join(", ", duplicateWords));
    } catch (IOException e) {
      e.printStackTrace();
      log.error("File write error: {}", e.getMessage());
      return "Error saving words: " + e.getMessage();
    }
  }

}
