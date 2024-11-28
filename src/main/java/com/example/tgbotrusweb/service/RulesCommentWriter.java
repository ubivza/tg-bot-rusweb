package com.example.tgbotrusweb.service;

import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.logic.enums.AdminsChannels;
import com.example.tgbotrusweb.logic.enums.Channels;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@Slf4j
public class RulesCommentWriter {
  private static final String germanRulesMessage = "⚠️Liebe Freunde unseres Kanals,\n"
      + "\n"
      + "Um sicherzustellen, dass der Dialog im Chat für alle Teilnehmer möglichst konstruktiv und sinnbringend verläuft, bitten wir um die Beachtung und Berücksichtigung folgender Regeln:\n"
      + "\n"
      + "https://t.me/infodefGERMANY/7063\n"
      + "\n"
      + "\uD83D\uDCCDAus aktuellem Anlass sehen wir die Befolgungspflicht der Regeln für jeden Nutzer als bindend, anderenfalls behält sich die Redaktion das Recht vor, ihn zu sperren.";

  private static final String statisticsMessageStart = "Today was parsed: ";
  private static final String statisticsMessageEnd = " comments in total";

  public void writeRulesInComments(Comment comment) {
    if (comment.getChannel() == Channels.GERMAN) {
      SendMessage sendRulesMessage = new SendMessage(String.valueOf(comment.getChannel().getId()), germanRulesMessage);
      sendRulesMessage.setReplyToMessageId(comment.getUpdate().getMessage().getMessageId());
      MessageEntity boldText = new MessageEntity("bold", germanRulesMessage.indexOf("Liebe"), germanRulesMessage.indexOf("sperren.") + 6);
      sendRulesMessage.setEntities(List.of(boldText));
      try {
        comment.getClient().execute(sendRulesMessage);
      } catch (TelegramApiException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public void sendStatsToAdmin(int counter, TelegramClient client) {
    log.info("Scheduled work started");
    SendMessage sendRulesMessage = new SendMessage(String.valueOf(Channels.ADMIN.getId()), statisticsMessageStart + counter + statisticsMessageEnd);
    try {
      client.execute(sendRulesMessage);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }
}
