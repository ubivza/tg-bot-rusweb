package com.example.tgbotrusweb.service.admin;

import com.example.tgbotrusweb.logic.enums.AdminsChannels;
import com.example.tgbotrusweb.logic.repository.WordFileRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
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

    TreeSet<String> existingWords = new TreeSet<>();
    existingWords.addAll(wordFileRepository.readWordsFromFile(fileName));
    String responseMessage;
    if (existingWords.isEmpty()) {
      responseMessage = "No words have been added yet.";
    } else {
      responseMessage = "Words list: "+ System.lineSeparator() + String.join(System.lineSeparator(), existingWords);
    }
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
      case RUSSIAN -> "russian_words";
      case ITALY -> "italian_words";
      case FRENCH -> "french_words";
      case GERMAN -> "german_words";
      case ENGLISH -> "english_words";
      case SPANISH -> "spanish_words";
      case GENERAL -> "general_words";
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
