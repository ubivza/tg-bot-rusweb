package com.example.tgbotrusweb.handler;

import static com.example.tgbotrusweb.keyboard.enums.Navigation.*;

import com.example.tgbotrusweb.entities.Binding;
import com.example.tgbotrusweb.entities.CommercialProject;
import com.example.tgbotrusweb.entities.Joiner;
import com.example.tgbotrusweb.entities.NonCommercialProject;
import com.example.tgbotrusweb.keyboard.NavigationInlineMarkup;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;

@Slf4j
public class DefaultHandler {
  private final NavigationInlineMarkup navigationMarkup;
  private final JoinerHandler joinerHandler;
  private final CommercialHandler commercialHandler;
  private final NonCommercialHandler nonCommercialHandler;
  private Map<Binding, Joiner> bindingJoiner = new HashMap<>();
  private Map<Binding, CommercialProject> bindingCommercial = new HashMap<>();
  private Map<Binding, NonCommercialProject> bindingNonCommercial = new HashMap<>();

  public DefaultHandler(NavigationInlineMarkup navigationMarkup, JoinerHandler joinerHandler,
      CommercialHandler commercialHandler, NonCommercialHandler nonCommercialHandler) {
    this.navigationMarkup = navigationMarkup;
    this.joinerHandler = joinerHandler;
    this.commercialHandler = commercialHandler;
    this.nonCommercialHandler = nonCommercialHandler;
  }

  @SneakyThrows
  public void handle(Update update, TelegramLongPollingSessionBot tgBot) {
    Long chatId = getId(update);
    if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/start")) {
      bindingCommercial.clear();
      bindingJoiner.clear();
      bindingNonCommercial.clear();
      tgBot.execute(navigationMarkup.createNavigationKeyboard(chatId));
    } else if (bindingJoiner.containsKey(Binding.builder().chatId(chatId).iter(0).type(JOIN).build())) {
      bindingJoiner = joinerHandler.handleName(update, bindingJoiner, chatId, tgBot);
    } else if (bindingJoiner.keySet().stream().anyMatch(x -> x.getIter() == 1 && x.getType() == JOIN)) {
      bindingJoiner = joinerHandler.handleSpec(update, bindingJoiner, chatId, tgBot);
    } else if (bindingJoiner.keySet().stream().anyMatch(x -> x.getIter() == 2 && x.getType() == JOIN)) {
      bindingJoiner = joinerHandler.handleContact(update, bindingJoiner, chatId, tgBot);
      System.out.println(bindingJoiner.size());
    } else if(bindingCommercial.containsKey(Binding.builder().chatId(chatId).iter(0).type(COMMERCIAL).build())) {
      bindingCommercial = commercialHandler.handleSphere(update, bindingCommercial, chatId, tgBot);
    } else if(bindingCommercial.keySet().stream().anyMatch(x -> x.getIter() == 1 && x.getType() == COMMERCIAL)) {
      bindingCommercial = commercialHandler.handleProject(update, bindingCommercial, chatId, tgBot);
    } else if(bindingCommercial.keySet().stream().anyMatch(x -> x.getIter() == 2 && x.getType() == COMMERCIAL)) {
      bindingCommercial = commercialHandler.handleName(update, bindingCommercial, chatId, tgBot);
    } else if(bindingCommercial.keySet().stream().anyMatch(x -> x.getIter() == 3 && x.getType() == COMMERCIAL)) {
      bindingCommercial = commercialHandler.handleContact(update, bindingCommercial, chatId, tgBot);
      System.out.println(bindingNonCommercial.size());
    } else if(bindingNonCommercial.containsKey(Binding.builder().chatId(chatId).iter(0).type(NON_COMMERCIAL).build())) {
      bindingNonCommercial = nonCommercialHandler.handleSphere(update, bindingNonCommercial, chatId, tgBot);
    } else if(bindingNonCommercial.keySet().stream().anyMatch(x -> x.getIter() == 1 && x.getType() == NON_COMMERCIAL)) {
      bindingNonCommercial = nonCommercialHandler.handleProject(update, bindingNonCommercial, chatId, tgBot);
    } else if(bindingNonCommercial.keySet().stream().anyMatch(x -> x.getIter() == 2 && x.getType() == NON_COMMERCIAL)) {
      bindingNonCommercial = nonCommercialHandler.handleName(update, bindingNonCommercial, chatId, tgBot);
    } else if(bindingNonCommercial.keySet().stream().anyMatch(x -> x.getIter() == 3 && x.getType() == NON_COMMERCIAL)) {
      bindingNonCommercial = nonCommercialHandler.handleContact(update, bindingNonCommercial, chatId, tgBot);
      System.out.println(bindingCommercial.size());
    } else if (update.hasCallbackQuery()) {
      var callData = update.getCallbackQuery().getData();
      if (callData.equals(ABOUT_US.getName())) {
        sendAboutUs(chatId, tgBot);
      } else if (callData.equals(JOIN.getName())) {
        sendJoin(chatId, tgBot);
      } else if (callData.equals(NON_COMMERCIAL.getName())) {
        sendNonCommercial(chatId, tgBot);
      } else if (callData.equals(COMMERCIAL.getName())) {
        sendCommercial(chatId, tgBot);
      } else {
        SendMessage message = SendMessage.builder()
            .chatId(chatId)
            .text("Пожалуйста, выберите пункт из сообщения выше")
            .build();
        tgBot.execute(message);
      }
    } else {
      SendMessage message = SendMessage.builder()
        .chatId(chatId)
        .text("Пожалуйста, нажмите /start, если хотите заполнить форму снова")
        .build();
      tgBot.execute(message);
      log.info("Something went wrong");
    }
  }
  private void sendAboutUs(long chatId, TelegramLongPollingSessionBot tgBot) {
    SendMessage sendMessage = SendMessage.builder()
        .chatId(chatId)
        .text(ABOUT_US.getMessage())
        .build();
    executeWrapper(sendMessage, tgBot);
  }

  private void sendJoin(long chatId, TelegramLongPollingSessionBot tgBot) {
    SendMessage sendMessage = SendMessage.builder()
        .chatId(chatId)
        .text(JOIN.getMessage())
        .build();
    executeWrapper(sendMessage, tgBot);
    bindingJoiner = new HashMap<>();
    bindingCommercial.clear();
    bindingNonCommercial.clear();
    Binding bind = Binding.builder().chatId(chatId).type(JOIN).iter(0).build();
    bindingJoiner.put(bind, new Joiner());
    executeWrapper(SendMessage.builder().text("Ваше имя:").chatId(chatId).build(), tgBot);
  }

  private void sendNonCommercial(long chatId, TelegramLongPollingSessionBot tgBot) {
    SendMessage sendMessage = SendMessage.builder()
        .chatId(chatId)
        .text(NON_COMMERCIAL.getMessage())
        .build();
    executeWrapper(sendMessage, tgBot);
    bindingNonCommercial = new HashMap<>();
    bindingJoiner.clear();
    bindingCommercial.clear();
    Binding bind = Binding.builder().chatId(chatId).type(NON_COMMERCIAL).iter(0).build();
    bindingNonCommercial.put(bind, new NonCommercialProject());
    executeWrapper(SendMessage.builder().text("Название и сфера деятельности проекта:").chatId(chatId).build(), tgBot);
  }

  private void sendCommercial(long chatId, TelegramLongPollingSessionBot tgBot) {
    SendMessage sendMessage = SendMessage.builder()
        .chatId(chatId)
        .text(COMMERCIAL.getMessage())
        .build();
    executeWrapper(sendMessage, tgBot);
    bindingCommercial = new HashMap<>();
    bindingJoiner.clear();
    bindingNonCommercial.clear();
    Binding bind = Binding.builder().chatId(chatId).type(COMMERCIAL).iter(0).build();
    bindingCommercial.put(bind, new CommercialProject());
    executeWrapper(SendMessage.builder().text("Название и сфера деятельности проекта:").chatId(chatId).build(), tgBot);
  }

  private Long getId(Update update) {
    return update.getMessage() != null ? update.getMessage().getChatId() : update.getCallbackQuery().getMessage().getChatId();
  }
  private void executeWrapper(SendMessage message, TelegramLongPollingSessionBot tgBot) {
    try {
      tgBot.execute(message);
    } catch (TelegramApiException e) {
      log.info("Exception while executing sendMessage");
      throw new RuntimeException(e);
    }
  }
}
