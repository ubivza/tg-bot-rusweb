package com.example.tgbotrusweb.service;

import com.example.tgbotrusweb.logic.domain.Comment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage.SendMessageBuilder;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class RulesCommentWriter {
  private static final String rulesMessage = "⚠️Liebe Freunde unseres Kanals,\n"
      + "\n"
      + "Um sicherzustellen, dass der Dialog im Chat für alle Teilnehmer möglichst konstruktiv und sinnbringend verläuft, bitten wir um die Beachtung und Berücksichtigung folgender Regeln:\n"
      + "\n"
      + "https://t.me/infodefGERMANY/7063\n"
      + "\n"
      + "\uD83D\uDCCDAus aktuellem Anlass sehen wir die Befolgungspflicht der Regeln für jeden Nutzer als bindend, anderenfalls behält sich die Redaktion das Recht vor, ihn zu sperren.\n"
      + "\n"
      + "\uD83D\uDCF1 InfoDefenseDEUTSCH (https://t.me/InfoDefGermany)\n"
      + "\uD83D\uDCF1 InfoDefense (https://t.me/infoDefALL)";
  public void writeRulesInComments(Comment comment) {
    //TODO make rules writer under every post relying on which channel it belongs to
    //need to make message using entities like this src/main/resources/example_message.png
    SendMessage sendRulesMessage = new SendMessage(String.valueOf(comment.getChannel().getId()), rulesMessage);
    sendRulesMessage.setReplyToMessageId(comment.getUpdate().getMessage().getMessageId());
    try {
      comment.getClient().execute(sendRulesMessage);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }
}
