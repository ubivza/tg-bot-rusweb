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
  private static String previousMediaGroupId = "";
  private static final Long CHANNEL_ID = 777000L;

  private Long commentsCounter = 0L;

  @Override
  public void handleUpdate(Comment comment) {
    if (isUpdateReply(comment)) {
      log.info("Got comment");
      commentsCounter++;
      next.handleUpdate(comment);
    } else {
      if (isSenderChannel(comment)) {
        if (isPreviousMessageNotTheSameMediaGroupId(comment)) {
          log.info("Writing rules under post");
          previousMediaGroupId = comment.getUpdate().getMessage().getMediaGroupId();
          commentWriter.writeRulesInComments(comment);
        }
      }
    }
  }

  private boolean isUpdateReply(Comment comment) {
    return comment.getUpdate().getMessage().isReply();
  }

  /*
    Проверяет совпадает ли mediaGroupId текущего сообщения с прошлым
   */
  private boolean isPreviousMessageNotTheSameMediaGroupId(Comment comment) {
    return previousMediaGroupId == null || !(previousMediaGroupId.equals(comment.getUpdate().getMessage().getMediaGroupId()));
  }

  private boolean isSenderChannel(Comment comment) {
    return comment.getUpdate().getMessage().getFrom().getId().equals(CHANNEL_ID);
  }
}
