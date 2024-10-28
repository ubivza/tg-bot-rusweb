package com.example.tgbotrusweb.logic;

import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.logic.interfaces.Handler;
import com.example.tgbotrusweb.service.CommentRemover;
import com.example.tgbotrusweb.utils.FileDataDownloader;
import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class GeneralWordsHandler extends Handler {
  private final CommentRemover commentRemover;
  private final List<String> generalWords = FileDataDownloader.readFromGeneralFile();

  @Override
  public void handleUpdate(Comment comment) {
    boolean isCommentNotBanned = true;
    for (String s : generalWords) {
      String commentTextLowerCase = comment.getUpdate().getMessage().getText().toLowerCase();
      if (Pattern.compile("\\b" + s.toLowerCase() + "\\b").matcher(commentTextLowerCase).find()) {
        log.info("Comment contains link and general word: " + s);
        isCommentNotBanned = false;
        commentRemover.handle(comment);
        break;
      }
    }
    if (isCommentNotBanned) {
      next.handleUpdate(comment);
    }
  }
}
