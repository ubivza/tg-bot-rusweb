package com.example.tgbotrusweb.handler;

import static com.example.tgbotrusweb.keyboard.enums.Navigation.NON_COMMERCIAL;

import com.example.tgbotrusweb.entities.Binding;
import com.example.tgbotrusweb.entities.NonCommercialProject;
import java.util.Map;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;

@Component
@Slf4j
public class NonCommercialHandler {
  private final String joinChatId = "-1001990644808";
  private final int joinThreadId = 2;

  private void executeWrapper(SendMessage message, TelegramLongPollingSessionBot tgBot) {
    try {
      tgBot.execute(message);
    } catch (TelegramApiException e) {
      log.info("Exception while executing sendMessage");
      throw new RuntimeException(e);
    }
  }

  public Map<Binding, NonCommercialProject> handleSphere(Update update, Map<Binding, NonCommercialProject> bindingNonCommercial,
      Long chatId, TelegramLongPollingSessionBot tgBot) {
    NonCommercialProject ncp = new NonCommercialProject();
    ncp.setNameAndSphere(update.getMessage().getText());
    bindingNonCommercial.clear();
    bindingNonCommercial.put(Binding.builder().type(NON_COMMERCIAL).iter(1).chatId(chatId).build(), ncp);
    bindingNonCommercial.keySet().forEach(System.out::println); //
    Stream.of(bindingNonCommercial.values()).forEach(System.out::println); //
    executeWrapper(SendMessage.builder().text("Что требуется разработать:").chatId(chatId).build(), tgBot);
    return bindingNonCommercial;
  }

  public Map<Binding, NonCommercialProject> handleProject(Update update, Map<Binding, NonCommercialProject> bindingNonCommercial,
      Long chatId, TelegramLongPollingSessionBot tgBot) {
    NonCommercialProject j = bindingNonCommercial.get(bindingNonCommercial.keySet().stream().findFirst().orElseThrow());
    j.setNeed(update.getMessage().getText());
    bindingNonCommercial.clear();
    bindingNonCommercial.put(Binding.builder().type(NON_COMMERCIAL).chatId(chatId).iter(2).build(), j);
    bindingNonCommercial.keySet().forEach(System.out::println); //
    Stream.of(bindingNonCommercial.values()).forEach(System.out::println); //
    executeWrapper(SendMessage.builder().text("Ваше имя:").chatId(chatId).build(), tgBot);
    return bindingNonCommercial;
  }

  public Map<Binding, NonCommercialProject> handleName(Update update, Map<Binding, NonCommercialProject> bindingNonCommercial,
      Long chatId, TelegramLongPollingSessionBot tgBot) {
    NonCommercialProject j = bindingNonCommercial.get(bindingNonCommercial.keySet().stream().findFirst().orElseThrow());
    j.setName(update.getMessage().getText());
    bindingNonCommercial.clear();
    bindingNonCommercial.put(Binding.builder().type(NON_COMMERCIAL).chatId(chatId).iter(3).build(), j);
    bindingNonCommercial.keySet().forEach(System.out::println); //
    Stream.of(bindingNonCommercial.values()).forEach(System.out::println); //
    executeWrapper(SendMessage.builder().text("Контакт для связи:").chatId(chatId).build(), tgBot);
    return bindingNonCommercial;
  }

  public Map<Binding, NonCommercialProject> handleContact(Update update, Map<Binding, NonCommercialProject> bindingNonCommercial,
      Long chatId, TelegramLongPollingSessionBot tgBot) {
    NonCommercialProject j = bindingNonCommercial.get(bindingNonCommercial.keySet().stream().findFirst().orElseThrow());
    j.setContact(update.getMessage().getText());
    bindingNonCommercial.clear();
    bindingNonCommercial.put(Binding.builder().type(NON_COMMERCIAL).chatId(chatId).iter(4).build(), j);
    sendData(bindingNonCommercial, tgBot);
    bindingNonCommercial.keySet().forEach(System.out::println); //
    Stream.of(bindingNonCommercial.values()).forEach(System.out::println); //
    executeWrapper(SendMessage.builder()
        .text("Спасибо, " + bindingNonCommercial.get(bindingNonCommercial.keySet().stream().findFirst().orElseThrow()).getName()
            + "! В ближайшее время мы с вами свяжемся (контакт для связи: "
            + bindingNonCommercial.get(bindingNonCommercial.keySet().stream().findFirst().orElseThrow()).getContact() + ")")
        .chatId(chatId).build(), tgBot);
    bindingNonCommercial.clear();
    return bindingNonCommercial;
  }

  private void sendData(Map<Binding, NonCommercialProject> bindingNonCommercial, TelegramLongPollingSessionBot tgBot) {
    SendMessage sendData = SendMessage.builder()
        .chatId(joinChatId)
        .messageThreadId(joinThreadId)
        .text("Новая заявка на волонтерскую разработку: " + bindingNonCommercial.get(bindingNonCommercial.keySet().stream().findFirst().orElseThrow()))
        .build();
    executeWrapper(sendData, tgBot);
  }
}
