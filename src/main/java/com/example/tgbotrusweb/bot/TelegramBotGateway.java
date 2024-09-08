package com.example.tgbotrusweb.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@Slf4j
public class TelegramBotGateway implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
  private final TelegramClient telegramClient;

  public TelegramBotGateway() {
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
    log.info("" + update);
    log.info("Chat id is: " + update.getMessage().getChatId());

    if (update.getMessage().getChatId() == (-1002412995088L) && update.getMessage().isReply()) {
      log.info("" + update.getMessage().getText());
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
    }

    /*if (update.getMessage().getChatId() == (-1002412995088L) && update.getMessage().hasCaption()) {
      if (update.getMessage().getCaption().startsWith("⚡️⚡️⚡️⚡️⚡️Verluste der ukrainischen Streitkräfte")) {
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
}
