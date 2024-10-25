package com.example.tgbotrusweb.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileDataDownloader {
  public static List<String> readFromGeneralFile() {
    try {
      return Files.readAllLines(Paths.get("src/main/resources/words/general_words"),
          StandardCharsets.UTF_8);
    } catch (IOException e) {
      System.out.println("File not found");
      throw new RuntimeException(e);
    }
  }

  public static List<String> readFromGermanFile() {
    try {
      return Files.readAllLines(Paths.get("src/main/resources/words/german_words"),
          StandardCharsets.UTF_8);
    } catch (IOException e) {
      System.out.println("File not found");
      throw new RuntimeException(e);
    }
  }

  public static List<String> readFromItalianFile() {
    try {
      return Files.readAllLines(Paths.get("src/main/resources/words/italian_words"),
          StandardCharsets.UTF_8);
    } catch (IOException e) {
      System.out.println("File not found");
      throw new RuntimeException(e);
    }
  }

  public static List<String> readFromFrenchFile() {
    try {
      return Files.readAllLines(Paths.get("src/main/resources/words/french_words"),
          StandardCharsets.UTF_8);
    } catch (IOException e) {
      System.out.println("File not found");
      throw new RuntimeException(e);
    }
  }

  public static List<String> readFromEnglishFile() {
    try {
      return Files.readAllLines(Paths.get("src/main/resources/words/english_words"),
          StandardCharsets.UTF_8);
    } catch (IOException e) {
      System.out.println("File not found");
      throw new RuntimeException(e);
    }
  }
}
