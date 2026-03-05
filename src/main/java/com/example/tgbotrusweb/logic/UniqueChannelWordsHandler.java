package com.example.tgbotrusweb.logic;

import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.logic.enums.Channels;
import com.example.tgbotrusweb.logic.interfaces.Handler;
import com.example.tgbotrusweb.service.CommentRemover;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.regex.Pattern;

import static com.example.tgbotrusweb.utils.FileDataDownloader.readFromEnglishFile;
import static com.example.tgbotrusweb.utils.FileDataDownloader.readFromFrenchFile;
import static com.example.tgbotrusweb.utils.FileDataDownloader.readFromGermanFile;
import static com.example.tgbotrusweb.utils.FileDataDownloader.readFromItalianFile;
import static com.example.tgbotrusweb.utils.FileDataDownloader.readFromRussianFile;
import static com.example.tgbotrusweb.utils.FileDataDownloader.readFromSpanishFile;

@Slf4j
@RequiredArgsConstructor
public class UniqueChannelWordsHandler extends Handler {
  private final NaiveCheckPassedHandler naiveCheckPassedHandler;
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
    boolean contains = false;

    switch (channel) {
      case SCHWARZER_HAUFEN -> contains = checkIfCommentContainsUniqueWord(germanWords, comment, channel.name());
      case WATCH_DOG -> contains = checkIfCommentContainsUniqueWord(englishWords, comment, channel.name());
      case GAVROCHE -> contains = checkIfCommentContainsUniqueWord(frenchWords, comment, channel.name());
      case RUSSIAN, RUSSIAN_LUGANSK -> contains = checkIfCommentContainsUniqueWord(russianWords, comment, channel.name());
    }

    if (contains) {
      commentRemover.handle(comment);
    } else {
      naiveCheckPassedHandler.handleUpdate(comment);
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

  private boolean checkIfCommentContainsUniqueWord(List<String> words, Comment comment, String language) {
    for (String s : words) {
      if (!s.isBlank()) {
        String commentTextLowerCase = getCommentTextWithoutInvisibleSeparator(comment);
        if (Pattern.compile(s.toLowerCase()).matcher(commentTextLowerCase).find()) {
          log.info("r " + language + " word: " + s);
          return true;
        }
      }
    }
    return false;
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
