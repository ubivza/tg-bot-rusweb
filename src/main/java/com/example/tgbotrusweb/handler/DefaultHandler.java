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
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;

@Slf4j
public class DefaultHandler {
  private final NavigationInlineMarkup navigationMarkup;
  private Map<Binding, Joiner> bindingJoiner = null;
  private Map<Binding, CommercialProject> bindingCommercial = null;
  private Map<Binding, NonCommercialProject> bindingNonCommercial = null;
  private CommercialProject project = new CommercialProject();
  private NonCommercialProject nonCommercialProject = new NonCommercialProject();

  public DefaultHandler(NavigationInlineMarkup navigationMarkup) {
    this.navigationMarkup = navigationMarkup;
  }

  @SneakyThrows
  public void handle(Update update, TelegramLongPollingSessionBot tgBot) {
    Long chatId = update.getMessage() != null ? update.getMessage().getChatId() : update.getCallbackQuery().getMessage().getChatId();
    if (bindingJoiner != null && bindingJoiner.containsKey(Binding.builder().chatId(chatId).iter(0).type(JOIN).build())) {
      Joiner j = new Joiner();
      j.setName(update.getMessage().getText());
      bindingJoiner.clear();
      bindingJoiner.put(Binding.builder().type(JOIN).chatId(chatId).iter(1).build(), j);
      bindingJoiner.keySet().forEach(System.out::println);
      tgBot.execute(SendMessage.builder().text("Ваша специализация и стек технологий:").chatId(chatId).build());
    } else if (bindingJoiner != null && bindingJoiner.keySet().stream().anyMatch(x -> x.getIter() == 1 && x.getType() == JOIN)) {
      Joiner j = bindingJoiner.get(bindingJoiner.keySet().stream().findFirst().orElseThrow());
      j.setSpecialization(update.getMessage().getText());
      bindingJoiner.clear();
      bindingJoiner.put(Binding.builder().type(JOIN).chatId(chatId).iter(2).build(), j);
      bindingJoiner.keySet().forEach(System.out::println);
      tgBot.execute(SendMessage.builder().text("Контакт для связи:").chatId(chatId).build());
    } else if (bindingJoiner != null && bindingJoiner.keySet().stream().anyMatch(x -> x.getIter() == 2 && x.getType() == JOIN)) {
      Joiner j = bindingJoiner.get(bindingJoiner.keySet().stream().findFirst().orElseThrow());
      j.setContact(update.getMessage().getText());
      bindingJoiner.clear();
      bindingJoiner.put(Binding.builder().type(JOIN).chatId(chatId).iter(3).build(), j);
      //send data
      bindingJoiner.keySet().forEach(System.out::println);
      tgBot.execute(SendMessage.builder().text("Ваши данные для связи: " + bindingJoiner.get(bindingJoiner.keySet().stream().findFirst().orElseThrow())).chatId(chatId).build());
      bindingJoiner.clear();
    } else if (update.hasCallbackQuery()) {
      var callData = update.getCallbackQuery().getData();
      if (callData.equals(ABOUT_US.getName())) {
        SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text(ABOUT_US.getMessage())
            .build();
        tgBot.execute(sendMessage);
      } else if (callData.equals(JOIN.getName())) {
        SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text(JOIN.getMessage())
            .build();
        tgBot.execute(sendMessage);
        bindingJoiner = new HashMap<>();
        Binding bind = Binding.builder().chatId(chatId).type(JOIN).iter(0).build();
        bindingJoiner.put(bind, new Joiner());
        tgBot.execute(SendMessage.builder().text("Ваше имя:").chatId(chatId).build());
      } else if (callData.equals(NON_COMMERCIAL.getName())) {
        SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text(NON_COMMERCIAL.getMessage())
            .build();
        tgBot.execute(sendMessage);
        //more
      } else if (callData.equals(COMMERCIAL.getName())) {
        SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text(COMMERCIAL.getMessage())
            .build();
        tgBot.execute(sendMessage);
        //more
      } else {
        SendMessage message = SendMessage.builder()
            .chatId(chatId)
            .text("Пожалуйста, выберите пункт из сообщения выше")
            .build();
        tgBot.execute(message);
      }
    } else if (update.hasMessage() && update.getMessage().hasText()) {
      var text = update.getMessage().getText();
      if (text.equals("/start")) {
        tgBot.execute(navigationMarkup.createNavigationKeyboard(chatId));
      }
    } else {
      log.info("Something went wrong");
    }
  }

  private void checkIfUpdateFromUser(Update update) {
    if (!update.getMessage().isUserMessage()) {
      log.info("Message not from chat, skip");
      throw new RuntimeException("Not from chat");
    }
  }
}
