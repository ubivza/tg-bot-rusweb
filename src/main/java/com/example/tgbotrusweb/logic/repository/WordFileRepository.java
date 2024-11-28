package com.example.tgbotrusweb.logic.repository;

import com.example.tgbotrusweb.logic.enums.AdminsChannels;
import com.example.tgbotrusweb.logic.enums.Channels;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WordFileRepository {

  private static final String RESOURCE_PATH = "src/main/resources/words/";

  private static Map<String, Set<String>> words = new HashMap<>();

  public Set<String> getWordSet(Long chatId) {
    return words.get(resolveFileNameWithChatId(chatId));
  }

  public Set<String> readWordsFromFile(Integer messageThreadId) {
    try (var lines = Files.lines(Paths.get(RESOURCE_PATH + resolveFileNameWithMessageThreadId(messageThreadId)))) {
      return lines.map(String::trim)
          .map(String::toLowerCase)
          .filter(line -> !line.isEmpty())
          .collect(Collectors.toCollection(TreeSet::new));
    } catch (IOException e) {
      return Collections.emptySet();
    }
  }

  public Set<String> readWordsFromFile(String fileName) {
    try (var lines = Files.lines(Paths.get(RESOURCE_PATH + fileName))) {
      return lines.map(String::trim)
          .map(String::toLowerCase)
          .filter(line -> !line.isEmpty())
          .collect(Collectors.toCollection(TreeSet::new));
    } catch (IOException e) {
      return Collections.emptySet();
    }
  }


  public String saveNewWords(Integer messageThreadId, Set<String> newWords, Set<String> duplicateWords) {
    if (newWords.isEmpty()) {
      String message = "No new words to add. These words already exist: " + String.join(", ", duplicateWords);
      log.info(message);
      return message;
    }

    String fileName = resolveFileNameWithMessageThreadId(messageThreadId);
    String message = save(fileName, newWords, duplicateWords);

    updateActualWordList(fileName);
    return message;
  }

  private void updateActualWordList(String fileName) {
    Set<String> ourList = readWordsFromFile(fileName);
    words.put(fileName, ourList);
    log.info("Words updated in memory");
  }

  private String save(String fileName, Set<String> newWords, Set<String> duplicateWords) {
    try (RandomAccessFile file = new RandomAccessFile(RESOURCE_PATH + fileName, "rw")) {
      long fileLength = file.length();
      boolean isNewLineNeeded = true;

      if (fileLength > 0) {
        file.seek(fileLength - 1);
        char lastChar = (char) file.readByte();

        isNewLineNeeded = lastChar != '\n';
      }

      file.seek(fileLength);

      if (isNewLineNeeded) {
        file.write(System.lineSeparator().getBytes());
      }

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


  private String resolveFileNameWithChatId(Long id) {
    return switch (Channels.getById(id)) {
      case ITALY -> "italy_words";
      case GERMAN -> "german_words";
      case FRENCH -> "french_words";
      case ENGLISH -> "english_words";
      case SPANISH -> "spanish_words";
      default -> throw new IllegalStateException("Message thread id is not valid");
    };
  }

  private String resolveFileNameWithMessageThreadId(Integer id) {
    return switch (AdminsChannels.getById(id)) {
      case ITALY -> "italy_words";
      case GERMAN -> "german_words";
      case FRENCH -> "french_words";
      case ENGLISH -> "english_words";
      case SPANISH -> "spanish_words";
      default -> throw new IllegalStateException("Message thread id is not valid");
    };
  }

  @PostConstruct
  private void updateWords() {
    log.info("Words updated in memory");
    words.put("italy_words", readWordsFromFile("italy_words"));
    words.put("french_words", readWordsFromFile("french_words"));
    words.put("german_words", readWordsFromFile("german_words"));
    words.put("english_words", readWordsFromFile("english_words"));
    words.put("general_words", readWordsFromFile("general_words"));
  }
}
