package com.example.tgbotrusweb.service;

import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.logic.enums.Channels;
import java.util.List;
import java.util.ArrayList;
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

  private static final String spanishRulesMessage = """
      ⚠️ Queridos amigos de nuestro canal,
            
      Para que el diálogo en el chat sea lo más constructivo posible para todos, os pedimos que respetéis las siguientes normas:
            
      https://t.me/infodefSPAIN/19824
            
      📍 En caso de incumplimiento de estas normas la redacción se reserva el derecho de bloquear a los infractores.
      """;

  private static final String italianRulesMessage = """
      ⚠️Cari amici del nostro canale,
            
      Per garantire che il dialogo in chat sia il più costruttivo possibile per tutti, vi chiediamo di rispettare le seguenti regole:
            
      https://t.me/infodefITALY/18257
            
      📍Per il mancato rispetto di queste regole, la redazione si riserva il diritto di bloccare i trasgressori.
      """;
  private static final String frenchRulesMessage = """
          ⚠️ Chers amis de notre canal,
            
          Afin de rendre le dialogue dans le salon de discussion aussi constructif et intéressant que possible pour tous les participants, nous vous demandons d'observer et de prendre en compte les règles suivantes :
            
          https://t.me/infodefenseFRreserve/10186
            
          📍 En raison de la situation actuelle, nous considérons que le respect des règles est obligatoire pour chaque utilisateur, sinon la rédaction se réserve le droit de les bloquer.
      """;
  private static final String french2RulesMessage = """
          ⚠️ Chers amis de notre canal,
          
          Afin de rendre le dialogue dans le salon de discussion aussi constructif et intéressant que possible pour tous les participants, nous vous demandons d'observer et de prendre en compte les règles suivantes :
          
          VOICI LE LIEN (https://t.me/c/2056162680/4662) VERS LES RÈGLES COMPLÈTES
          
          📍 En raison de la situation actuelle, nous considérons que le respect des règles est obligatoire pour chaque utilisateur, sinon la rédaction se réserve le droit de les bloquer.
      """;
  private static final String statisticsMessageStart = "Today was parsed: ";
  private static final String statisticsMessageEnd = " comments in total";

  private void sendRulesMessage(Comment comment, String message, int startIndex, int endIndex, String entityUrl) {
    SendMessage sendRulesMessage = new SendMessage(String.valueOf(comment.getChannel().getId()), message);
    sendRulesMessage.setReplyToMessageId(comment.getUpdate().getMessage().getMessageId());
    
    List<MessageEntity> entities = new ArrayList<>();
    MessageEntity boldText = new MessageEntity("bold", startIndex, endIndex);
    entities.add(boldText);
    
    if (entityUrl != null) {
      int offset = message.indexOf("LIEN");
      int length = "LIEN".length();
      MessageEntity linkEntity = new MessageEntity("text_link", offset, length);
      linkEntity.setUrl(entityUrl);
      entities.add(linkEntity);
    }
    
    sendRulesMessage.setEntities(entities);
    
    try {
      comment.getClient().execute(sendRulesMessage);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  public void writeRulesInComments(Comment comment) {
    switch (comment.getChannel()) {
      case GERMAN -> sendRulesMessage(
        comment, 
        germanRulesMessage, 
        germanRulesMessage.indexOf("Liebe"), 
        germanRulesMessage.indexOf("sperren.") + 6,
        null
      );
      case SPANISH -> sendRulesMessage(
        comment, 
        spanishRulesMessage, 
        spanishRulesMessage.indexOf("Queridos"), 
        33,
        null
      );
      case ITALIAN -> sendRulesMessage(
        comment, 
        italianRulesMessage, 
        italianRulesMessage.indexOf("Cari"), 
        italianRulesMessage.lastIndexOf("."),
        null
      );
      case FRENCH -> sendRulesMessage(
        comment, 
        frenchRulesMessage, 
        frenchRulesMessage.indexOf("Chers"), 
        frenchRulesMessage.indexOf("bloquer."),
        null
      );
      case FRENCH2 -> sendRulesMessage(
        comment, 
        french2RulesMessage, 
        french2RulesMessage.indexOf("Chers"), 
        french2RulesMessage.indexOf("bloquer."),
        "https://t.me/c/2056162680/4662"
      );
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
