package com.example.tgbotrusweb.logic;

import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.logic.domain.CommentStatistics;
import com.example.tgbotrusweb.logic.enums.Channels;
import com.example.tgbotrusweb.logic.interfaces.Handler;
import com.example.tgbotrusweb.service.RulesCommentWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@RequiredArgsConstructor
public class ReplyHandler extends Handler {
  private final RulesCommentWriter commentWriter;
  private static final Map<Channels, CommentStatistics> commentsCounterMap = new HashMap<>();
  private static String previousMediaGroupId = "";
  private static final Long CHANNEL_ID = 777000L;

  static {
    for (Channels channel : Channels.values()) {
      commentsCounterMap.put(channel, CommentStatistics.builder()
          .totalComments(new AtomicInteger(0))
          .totalSpamComments(new AtomicInteger(0))
          .build());
    }
  }

  @Override
  public void handleUpdate(Comment comment) {
    var commentChannel = comment.getChannel();

    if (isSenderChannel(comment)) {
      if (isPreviousMessageNotTheSameMediaGroupId(comment)) {
        log.info("Writing rules under post in " + commentChannel);
        previousMediaGroupId = comment.getUpdate().getMessage().getMediaGroupId();
        commentWriter.writeRulesInComments(comment);
      }
      return;
    }

    if (commentChannel != null) {
      commentsCounterMap.get(commentChannel).getTotalComments().incrementAndGet();
    }

    log.info("Got comment from " + commentChannel);

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
    commentsCounterMap.replaceAll((key, value) -> CommentStatistics.builder()
        .totalComments(new AtomicInteger(0))
        .totalSpamComments(new AtomicInteger(0))
        .build());
  }

  public void sendStatsToAdmin(TelegramClient telegramClient) {
    commentWriter.sendStatsToAdmin(commentsCounterMap, telegramClient);
  }

  public static void incrementSpamCounter(Channels channel) {
    commentsCounterMap.get(channel).getTotalSpamComments().incrementAndGet();
  }

}
