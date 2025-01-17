package com.example.tgbotrusweb.logic;

import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.logic.interfaces.Handler;
import com.example.tgbotrusweb.service.RulesCommentWriter;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@RequiredArgsConstructor
public class ReplyHandler extends Handler {
  private final RulesCommentWriter commentWriter;
  private static final AtomicInteger commentsCounter = new AtomicInteger();
  private static String previousMediaGroupId = "";
  private static final Long CHANNEL_ID = 777000L;

  @Override
  public void handleUpdate(Comment comment) {
    if (isSenderChannel(comment)) {
      if (isPreviousMessageNotTheSameMediaGroupId(comment)) {
        log.info("Writing rules under post in " + comment.getChannel());
        previousMediaGroupId = comment.getUpdate().getMessage().getMediaGroupId();
        commentWriter.writeRulesInComments(comment);
      }
      return;
    }

    log.info("Got comment");
    commentsCounter.incrementAndGet();
    next.handleUpdate(comment);
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

  public void refreshCommentsCount() {
    commentsCounter.set(0);
  }

  public void sendStatsToAdmin(TelegramClient telegramClient) {
    commentWriter.sendStatsToAdmin(commentsCounter.get(), telegramClient);
  }
}
