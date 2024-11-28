package com.example.tgbotrusweb.service.admin;

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

  public String processAndSaveWords(String input, Integer messageThreadId) {
    Set<String> inputWords = parseWords(input);

    Set<String> existingWords = wordFileRepository.readWordsFromFile(messageThreadId);//TODO из мапп теперь читаем //TODO TREESET
    Set<String> duplicateWords = findDuplicates(inputWords, existingWords);
    Set<String> newWords = findNewWords(inputWords, existingWords);

    return wordFileRepository.saveNewWords(messageThreadId, newWords, duplicateWords);
  }

  public String getWordsForChannel(Integer messageThreadId) {
    Set<String> existingWords = wordFileRepository.readWordsFromFile(messageThreadId);
    String responseMessage;
    if (existingWords.isEmpty()) {
      responseMessage = "No words have been added yet.";
    } else {
      responseMessage = "Words list: " + System.lineSeparator() + String.join(System.lineSeparator(), existingWords);
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
