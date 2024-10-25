package com.example.tgbotrusweb.logic;

import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.logic.interfaces.Handler;
import com.example.tgbotrusweb.service.CommentRemover;
import com.example.tgbotrusweb.utils.FileDataDownloader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class GeneralWordsHandler extends Handler {
  private final CommentRemover commentRemover;
  private final List<String> generalWords = FileDataDownloader.readFromGeneralFile();

  @Override
  public void handleUpdate(Comment comment) {
    boolean isCommentBanned = true;
    for (String s : generalWords) {
      String commentTextLowerCase = comment.getUpdate().getMessage().getText().toLowerCase();
      if (commentTextLowerCase.contains(s.toLowerCase())) {
        log.info("Comment contains link and general word: " + s);
        isCommentBanned = false;
        commentRemover.handle(comment);
      }
    }
    if (isCommentBanned) {
      next.handleUpdate(comment);
    }
  }
}
