package com.example.tgbotrusweb.logic;

import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.logic.interfaces.Handler;
import com.example.tgbotrusweb.service.CommentRemover;
import com.example.tgbotrusweb.utils.FileDataDownloader;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class GeneralWordsHandler extends Handler {
  private final CommentRemover commentRemover;
  private static List<String> generalWords;
  private static final String REGEX_INVISIBLE_SYMBOL = "\u2063";

  @Override
  public void handleUpdate(Comment comment) {
    boolean isCommentNotBanned = true;
    for (String s : generalWords) {
      if (!s.isBlank()) {
        String commentTextLowerCase = getCommentTextWithoutInvisibleSeparator(comment);
        if (Pattern.compile("\\b" + s.toLowerCase().trim() + "\\b").matcher(commentTextLowerCase).find()) {
          log.info("Comment contains link and general word: " + s);
          isCommentNotBanned = false;
          commentRemover.handle(comment);
          break;
        }
      }
    }
    if (isCommentNotBanned) {
      next.handleUpdate(comment);
    }
  }

  @PostConstruct
  public void updateWords() {
    log.info("General words updated in memory");
    generalWords = FileDataDownloader.readFromGeneralFile();
  }

  private static String getCommentTextWithoutInvisibleSeparator(Comment comment) {
    return comment.getUpdate().getMessage().getText().toLowerCase().replaceAll(REGEX_INVISIBLE_SYMBOL, "");
  }
}
