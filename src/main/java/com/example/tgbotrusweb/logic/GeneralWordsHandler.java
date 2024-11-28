package com.example.tgbotrusweb.logic;

import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.logic.interfaces.Handler;
import com.example.tgbotrusweb.logic.repository.WordFileRepository;
import com.example.tgbotrusweb.service.CommentRemover;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class GeneralWordsHandler extends Handler {

  private final WordFileRepository repository;
  private final CommentRemover commentRemover;
  private static final String REGEX_INVISIBLE_SYMBOL = "\u2063";

  @Override
  public void handleUpdate(Comment comment) {
    boolean isCommentNotBanned = true;
    for (String s : repository.getWordSet(comment.getChatId())) {
      if (!s.isBlank()) {
        String commentTextLowerCase = getCommentTextWithoutInvisibleSeparator(comment);
        if (Pattern.compile(s.toLowerCase().trim()).matcher(commentTextLowerCase).find()) {
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

  private static String getCommentTextWithoutInvisibleSeparator(Comment comment) {
    return comment.getUpdate().getMessage().getText().toLowerCase().replaceAll(REGEX_INVISIBLE_SYMBOL, "");
  }
}
