package com.example.tgbotrusweb.logic;

import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.logic.interfaces.Handler;
import com.example.tgbotrusweb.logic.repository.WordFileRepository;
import com.example.tgbotrusweb.service.CommentRemover;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UniqueChannelWordsHandler extends Handler {

  private final WordFileRepository repository;
  private final CommentRemover commentRemover;
  private static final String REGEX_INVISIBLE_SYMBOL = "\u2063";

  @Override
  public void handleUpdate(Comment comment) {
    Set<String> wordSet = repository.getWordSet(comment.getChatId());
    checkIfCommentContainsUniqueWord(wordSet, comment);
  }

  private void checkIfCommentContainsUniqueWord(Set<String> words, Comment comment) {
    for (String s : words) {
      if (!s.isBlank()) {
        String commentTextLowerCase = getCommentTextWithoutInvisibleSeparator(comment);
        if (Pattern.compile(s.toLowerCase()).matcher(commentTextLowerCase).find()) {
          //log.info("r " + language + " word: " + s); TODO
          commentRemover.handle(comment);
          break;
        }
      }
    }
  }

  private static String getCommentTextWithoutInvisibleSeparator(Comment comment) {
    return comment.getUpdate().getMessage().getText().toLowerCase().replaceAll(REGEX_INVISIBLE_SYMBOL, "");
  }

}
