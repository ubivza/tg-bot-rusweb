package com.example.tgbotrusweb.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileDataDownloader {

  private static final String RESOURCE_PATH = "src/main/resources/words/";

  public static List<String> readFromFile(String filename) {
    try {
      return Files.readAllLines(Paths.get(RESOURCE_PATH + filename), StandardCharsets.UTF_8);
    } catch (IOException e) {
      System.err.println("File not found: " + filename);
      throw new RuntimeException(e);
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
