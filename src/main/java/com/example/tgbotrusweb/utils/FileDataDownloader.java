package com.example.tgbotrusweb.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class FileDataDownloader {

  private static List<String> readFromFile(String fileName) {
    try {
      ClassPathResource resource = new ClassPathResource("words/" + fileName);

      try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
        return reader.lines().collect(Collectors.toList());
      }
    } catch (IOException e) {
      throw new RuntimeException("File not found: " + fileName, e);
    }
  }

  public static List<String> readFromGeneralFile() {
    return readFromFile("general_words");
  }

  public static List<String> readFromGermanFile() {
    return readFromFile("german_words");
  }

  public static List<String> readFromItalianFile() {
    return readFromFile("italian_words");
  }

  public static List<String> readFromFrenchFile() {
    return readFromFile("french_words");
  }

  public static List<String> readFromSpanishFile() {
    return readFromFile("spanish_words");
  }

  public static List<String> readFromEnglishFile() {
    return readFromFile("english_words");
  }

  public static List<String> readFromRussianFile() {
    return readFromFile("russian_words");
  }
}
