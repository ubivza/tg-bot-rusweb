package com.example.tgbotrusweb.bot;

import com.example.tgbotrusweb.logic.ReplyHandler;
import com.example.tgbotrusweb.logic.admin.WordHandler;
import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.logic.domain.admin.AdminMessage;
import com.example.tgbotrusweb.logic.enums.AdminsChannels;
import com.example.tgbotrusweb.logic.enums.Channels;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
  private final WordHandler wordHandler;
  private static final Executor executor = Executors.newFixedThreadPool(50);
  private static final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
  private static final int initialDelay = 23 - LocalDateTime.now().getHour();

  public TelegramBotGateway(ReplyHandler replyHandler, WordHandler wordHandler) {
    this.replyHandler = replyHandler;
    this.wordHandler = wordHandler;
    telegramClient = new OkHttpTelegramClient(getBotToken());
  }

  @Override
  public String getBotToken() {
    return "7945101745:AAEqibTBhe355A7G2fY3uDE5OIp4yPUfGb8";
  } //7945101745:AAEqibTBhe355A7G2fY3uDE5OIp4yPUfGb8

  @Override
  public LongPollingUpdateConsumer getUpdatesConsumer() {
    return this;
  }

  @Override
  public void consume(Update update) {
    if (update.hasMessage()) {
      if (isUpdateFromOurChannel(update)) {
        replyHandler.handleUpdate(Comment.builder()
            .update(update)
            .client(telegramClient)
            .channel(getChannel(update))
            .build());
      } else if (isUpdateFromOurAdminsChannel(update)) {
        wordHandler.handleCommand(AdminMessage.builder()
            .update(update)
            .client(telegramClient)
            .channel(getAdminsChannel(update))
            .build());
      }
    }

    /*Runnable task = () -> {
      if (isUpdateFromOurChannel(update)) {
        replyHandler.handleUpdate(Comment.builder()
            .update(update)
            .client(telegramClient)
            .channel(getChannel(update))
            .build());
      }
    };
    executor.execute(task);*/
  }

  @AfterBotRegistration
  public void afterRegistration(BotSession botSession) {
    log.info("Registered bot running state is: " + botSession.isRunning());
    log.info("Available processors: " + Runtime.getRuntime().availableProcessors());
    log.info("Total memory: " + Runtime.getRuntime().totalMemory());
    log.info("Scheduler is set");
    service.scheduleAtFixedRate(() -> replyHandler.sendStatsToAdmin(telegramClient), initialDelay, 24, TimeUnit.HOURS);
    service.scheduleAtFixedRate(replyHandler::refreshCommentsCount, initialDelay, 24, TimeUnit.HOURS);
    log.info("init delay: " + initialDelay);
  }

  private Channels getChannel(Update update) {
    if (Objects.equals(update.getMessage().getChatId(), Channels.TEST.getId())) {
      return Channels.TEST;
    } else if (Objects.equals(update.getMessage().getChatId(), Channels.ITALIAN.getId())) {
      return Channels.ITALIAN;
    } else if (Objects.equals(update.getMessage().getChatId(), Channels.GERMAN.getId())) {
      return Channels.GERMAN;
    } else if (Objects.equals(update.getMessage().getChatId(), Channels.FRENCH.getId())) {
      return Channels.FRENCH;
    } else if (Objects.equals(update.getMessage().getChatId(), Channels.FRENCH2.getId())) {
      return Channels.FRENCH2;
    } else if (Objects.equals(update.getMessage().getChatId(), Channels.ENGLISH.getId())) {
      return Channels.ENGLISH;
    } else if (Objects.equals(update.getMessage().getChatId(), Channels.SPANISH.getId())) {
      return Channels.SPANISH;
    } else {
      throw new RuntimeException("Chat id is not valid");
    }
  }

  private boolean isUpdateFromOurChannel(Update update) {
    return Arrays.stream(Channels.values()).filter(x -> x != Channels.ADMIN).map(Channels::getId).toList().contains(update.getMessage().getChatId());
  }

  private AdminsChannels getAdminsChannel(Update update) {
    if (Objects.equals(update.getMessage().getMessageThreadId(), AdminsChannels.ITALY.getId())) {
      return AdminsChannels.ITALY;
    } else if (Objects.equals(update.getMessage().getMessageThreadId(), AdminsChannels.GERMAN.getId())) {
      return AdminsChannels.GERMAN;
    } else if (Objects.equals(update.getMessage().getMessageThreadId(), AdminsChannels.FRENCH.getId())) {
      return AdminsChannels.FRENCH;
    } else if (Objects.equals(update.getMessage().getMessageThreadId(), AdminsChannels.ENGLISH.getId())) {
      return AdminsChannels.ENGLISH;
    } else if (Objects.equals(update.getMessage().getMessageThreadId(), AdminsChannels.GENERAL.getId())) {
      return AdminsChannels.GENERAL;
    } else if (Objects.equals(update.getMessage().getMessageThreadId(), AdminsChannels.SPANISH.getId())) {
      return AdminsChannels.SPANISH;
    } else {
      throw new RuntimeException("Message thread id is not valid");
    }
  }

  private boolean isUpdateFromOurAdminsChannel(Update update) {
    return update.getMessage().getChatId().equals(Channels.ADMIN.getId());
  }

}
