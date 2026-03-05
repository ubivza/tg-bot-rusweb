package com.example.tgbotrusweb.service;

import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.logic.domain.CommentStatistics;
import com.example.tgbotrusweb.logic.enums.Channels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
  private static String russianRulesMessage = """
      ⚠️⚠️⚠️Дорогие друзья нашего канала,
      
      Мы всегда рады вашему участию в диалоге и обсуждении актуальных тем в комментариях.
      
      Однако, чтобы диалог для всех участников был максимально конструктивным и содержательным, просим учитывать и соблюдать следующие правила:
      
      🚫В чате запрещается:
      
      1️⃣ Систематическое, умышленное распространение фейковых новостей и паники.
      
      2️⃣ Троллинг, оскорбления и провокации в отношении других участников.
      
      3️⃣ Нецензурная лексика, спам, мошеннические сообщения (например, о даркнете или криптосделках), порнография, педофилия, пропаганда LGBTIQA.
      
      4️⃣ Распространение этнической нетерпимости, расизма, национализма и нацизма. Использование нацистской символики в контексте пропаганды и оправдания нацистских преступлений.
      
      5️⃣ Пропаганда и оправдание нацистской Германии, а именно: уничтожение славян, отрицание Холокоста и другие преступления; Гитлер, «приветствие Гитлера», соответствующая символика; уравнивание Путина или Сталина с Гитлером.
      
      6️⃣ Прославление нацистских преступлений, лозунгов и практик Украины, а именно: «Слава Украине», «Ваффен-СС „Галиция“», соответствующая символика и т. п.
      
      7️⃣ Оскорбление религиозных чувств человека, а также богохульство и публикация материалов, оскорбляющих мировые религии (см. Швецию).
      
      8️⃣ Призывы к насильственным демонстрациям и к беспорядкам с использованием оружия.
      
      9️⃣ Обсуждение правил, а также действий администрации в чате. Оскорбления в адрес администраторов/модераторов канала. Попытки обходить наложенные ограничения.
      
      1️⃣0️⃣ Недопустимо выдвигать утверждения без указания проверяемых источников.
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
      case RUSSIAN -> sendRulesMessage(
          comment,
          russianRulesMessage,
          russianRulesMessage.indexOf("Дорогие"),
          russianRulesMessage.indexOf("источников.") + "источников.".length(),
          null
      );
    }
  }

  public void sendStatsToAdmin(Map<Channels, CommentStatistics> commentsCounterMap, TelegramClient client) {
    log.info("Scheduled work started");

    if (commentsCounterMap.isEmpty()) {
      log.warn("Comments counter map is empty");
      return;
    }

    StringBuilder statsMessage = new StringBuilder(statisticsMessageStart + "\n\n");
    int totalComments = 0;
    int spamComments = 0;

    for (Map.Entry<Channels, CommentStatistics> entry : commentsCounterMap.entrySet()) {
      if (entry.getKey() != Channels.ADMIN && entry.getKey() != Channels.TEST) {
        int count = entry.getValue().getTotalComments().get();
        totalComments += count;
        statsMessage.append(entry.getKey().name())
            .append(": ")
            .append(count)
            .append(" comments");

        int spamCount = entry.getValue().getTotalSpamComments().get();
        spamComments += spamCount;
        statsMessage.append("; SPAM: ")
            .append(spamCount)
            .append(System.lineSeparator());
      }
    }

    statsMessage.append("\nTotal: ")
        .append(totalComments)
        .append(statisticsMessageEnd)
        .append(System.lineSeparator())
        .append("Spam: ")
        .append(spamComments)
        .append(" of them are spam");

    SendMessage sendStatsMessage = new SendMessage(
        String.valueOf(Channels.ADMIN.getId()),
        statsMessage.toString()
    );

    try {
      client.execute(sendStatsMessage);
    } catch (TelegramApiException e) {
      log.error("Error sending stats message", e);
      throw new RuntimeException(e);
    }
  }
}
