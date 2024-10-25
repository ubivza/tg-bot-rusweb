package com.example.tgbotrusweb.bot;

import com.example.tgbotrusweb.logic.ReplyHandler;
import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.logic.enums.Channels;
import java.util.Arrays;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@Slf4j
public class TelegramBotGateway implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
  private final TelegramClient telegramClient;

  private final ReplyHandler replyHandler;

  public TelegramBotGateway(ReplyHandler replyHandler) {
    this.replyHandler = replyHandler;
    telegramClient = new OkHttpTelegramClient(getBotToken());
  }

  @Override
  public String getBotToken() {
    return "6506450289:AAGd-Z7XXU8WjhtNZjfCTagkr5ljoATyTsk";
  }

  @Override
  public LongPollingUpdateConsumer getUpdatesConsumer() {
    return this;
  }

  @Override
  public void consume(Update update) {
    //TODO
    //here check if update is coming from one of our channels, else ignore
    log.info("Chat id is: " + update.getMessage().getChatId());
    log.info("" + update.getMessage().getFrom());
    if (isUpdateFromOurChannel(update)) {
      replyHandler.handleUpdate(Comment.builder()
          .update(update)
          .client(telegramClient)
          .channel(getChannel(update))
          .build());
    }




    /*log.info("" + update);
    log.info("Chat id is: " + update.getMessage().getChatId());

    if (update.getMessage().getChatId() == (-1002412995088L) && update.getMessage().isReply()) {
      log.info("" + update.getMessage().getText());
      log.info("" + update.getMessage().getFrom());
      boolean isContainsLink = update.getMessage().getText().contains("http");
      if (isContainsLink) {
        log.info("Message contains link");
        DeleteMessage deleteMessageRequest = new DeleteMessage(String.valueOf(-1002412995088L), update.getMessage().getMessageId());
        try {
          telegramClient.execute(deleteMessageRequest);
        } catch (TelegramApiException e) {
          throw new RuntimeException(e);
        }
      }
    }*/
  }

  @AfterBotRegistration
  public void afterRegistration(BotSession botSession) {
    System.out.println("Registered bot running state is: " + botSession.isRunning());
  }

  private Channels getChannel(Update update) {
    if (Objects.equals(update.getMessage().getChatId(), Channels.TEST.getId())) {
      return Channels.TEST;
    } else if (Objects.equals(update.getMessage().getChatId(), Channels.ITALY.getId())) {
      return Channels.ITALY;
    } else if (Objects.equals(update.getMessage().getChatId(), Channels.GERMAN.getId())) {
      return Channels.GERMAN;
    } else if (Objects.equals(update.getMessage().getChatId(), Channels.FRENCH.getId())) {
      return Channels.FRENCH;
    } else if (Objects.equals(update.getMessage().getChatId(), Channels.ENGLISH.getId())) {
      return Channels.ENGLISH;
    } else {
      throw new RuntimeException("Chat id is not valid");
    }
  }

  private boolean isUpdateFromOurChannel(Update update) {
    return Arrays.stream(Channels.values()).filter(x -> x != Channels.ADMIN).map(Channels::getId).toList().contains(update.getMessage().getChatId());
  }
}
