package com.example.tgbotrusweb.logic;

import static com.example.tgbotrusweb.utils.FileDataDownloader.*;

import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.logic.enums.Channels;
import com.example.tgbotrusweb.logic.interfaces.Handler;
import com.example.tgbotrusweb.service.CommentRemover;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UniqueChannelWordsHandler extends Handler {
  private final CommentRemover commentRemover;
  private static List<String> germanWords;
  private static List<String> englishWords;
  private static List<String> italianWords;
  private static List<String> frenchWords;
  private static List<String> spanishWords;
  private static List<String> russianWords;
  private static final String REGEX_INVISIBLE_SYMBOLS = "[\u2063\u2062]";

  @Override
  public void handleUpdate(Comment comment) {
    Channels channel = comment.getChannel();
    log.info(channel + "");
    switch (channel) {
      case SCHWARZER_HAUFEN -> checkIfCommentContainsUniqueWord(germanWords, comment, channel.name());
      case WATCH_DOG -> checkIfCommentContainsUniqueWord(englishWords, comment, channel.name());
      case GAVROCHE -> checkIfCommentContainsUniqueWord(frenchWords, comment, channel.name());
      case RUSSIAN, RUSSIAN_LUGANSK -> checkIfCommentContainsUniqueWord(russianWords, comment, channel.name());
    }
  }

  public void updateEnglishWords() {
    log.info("English words updated in memory");
    englishWords = readFromEnglishFile();
  }

  public void updateGermanWords() {
    log.info("German words updated in memory");
    germanWords = readFromGermanFile();
  }

  public void updateItalianWords() {
    log.info("Italian words updated in memory");
    italianWords = readFromItalianFile();
  }

  public void updateFrenchWords() {
    log.info("French words updated in memory");
    frenchWords = readFromFrenchFile();
  }

  public void updateRussianWords() {
    log.info("Russian words updated in memory");
    russianWords = readFromRussianFile();
  }

  private void checkIfCommentContainsUniqueWord(List<String> words, Comment comment, String language) {
    for (String s : words) {
      if (!s.isBlank()) {
        String commentTextLowerCase = getCommentTextWithoutInvisibleSeparator(comment);
        if (Pattern.compile(s.toLowerCase()).matcher(commentTextLowerCase).find()) {
          log.info("r " + language + " word: " + s);
          commentRemover.handle(comment);
          break;
        }
      }
    }
  }

  private static String getCommentTextWithoutInvisibleSeparator(Comment comment) {
    return comment.getUpdate().getMessage().getText().toLowerCase().replaceAll(REGEX_INVISIBLE_SYMBOLS, "");
  }

  @PostConstruct
  private void updateWords() {
    log.info("Unique words updated in memory");
    germanWords = readFromGermanFile();
    englishWords = readFromEnglishFile();
    italianWords = readFromItalianFile();
    frenchWords = readFromFrenchFile();
    spanishWords = readFromSpanishFile();
    russianWords = readFromRussianFile();
  }

  public void updateSpanishWords() {
    log.info("Spanish words updated in memory");
    spanishWords = readFromSpanishFile();
  }
}
