package com.example.tgbotrusweb.service.admin;

import com.example.tgbotrusweb.logic.enums.AdminsChannels;
import com.example.tgbotrusweb.logic.repository.WordFileRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WordProcessingService {

  private final WordFileRepository wordFileRepository;

  public WordProcessingService(WordFileRepository wordFileRepository) {
    this.wordFileRepository = wordFileRepository;
  }

  public String processAndSaveWords(String input, AdminsChannels channel) {
    Set<String> inputWords = parseWords(input);
    String fileName = resolveFileName(channel);

    Set<String> existingWords = wordFileRepository.readWordsFromFile(fileName);
    Set<String> duplicateWords = findDuplicates(inputWords, existingWords);
    Set<String> newWords = findNewWords(inputWords, existingWords);

    return wordFileRepository.saveNewWords(fileName, newWords, duplicateWords);
  }

  public String getWordsForChannel(AdminsChannels channel) {
    String fileName = resolveFileName(channel);

    Set<String> existingWords = wordFileRepository.readWordsFromFile(fileName);
    String responseMessage;
    if (existingWords.isEmpty()) {
      responseMessage = "No words have been added yet.";
    } else {
      responseMessage = "Words list: " + String.join(", ", existingWords);
    }
    log.info(responseMessage);
    return responseMessage;
  }

  private Set<String> parseWords(String input) {
    if (input.startsWith("/add ")) {
      input = input.substring(5);
    }
    return Arrays.stream(input.split(";\\s*"))
        .map(String::trim)
        .filter(word -> !word.isEmpty())
        .collect(Collectors.toSet());
  }

  private String resolveFileName(AdminsChannels channel) {
    return switch (channel) {
      case ITALY -> "src/main/resources/words/italian_words";
      case FRENCH -> "src/main/resources/words/french_words";
      case GERMAN -> "src/main/resources/words/german_words";
      case ENGLISH -> "src/main/resources/words/english_words";
      case GENERAL -> "src/main/resources/words/general_words";
    };
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

}
