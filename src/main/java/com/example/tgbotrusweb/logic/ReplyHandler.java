package com.example.tgbotrusweb.logic;

import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.logic.interfaces.Handler;
import com.example.tgbotrusweb.service.RulesCommentWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ReplyHandler extends Handler {
  private final RulesCommentWriter commentWriter;

  private Long commentsCounter = 0L;

  @Override
  public void handleUpdate(Comment comment) {
    if (isUpdateReply(comment)) {
      log.info("Got comment");
      commentsCounter++;
      next.handleUpdate(comment);
    } else {
      log.info("Writing rules under post");
      commentWriter.writeRulesInComments(comment);
    }
  }

  private boolean isUpdateReply(Comment comment) {
    return comment.getUpdate().getMessage().isReply();
  }
}
