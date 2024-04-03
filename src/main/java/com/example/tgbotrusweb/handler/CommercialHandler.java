package com.example.tgbotrusweb.handler;

import static com.example.tgbotrusweb.keyboard.enums.Navigation.COMMERCIAL;

import com.example.tgbotrusweb.entities.Binding;
import com.example.tgbotrusweb.entities.CommercialProject;
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
public class CommercialHandler {
  private final String joinChatId = "-1001990644808";
  private final int joinThreadId = 3;

  private void executeWrapper(SendMessage message, TelegramLongPollingSessionBot tgBot) {
    try {
      tgBot.execute(message);
    } catch (TelegramApiException e) {
      log.info("Exception while executing sendMessage");
      throw new RuntimeException(e);
    }
  }

  public Map<Binding, CommercialProject> handleSphere(Update update, Map<Binding, CommercialProject> bindingCommercial,
      Long chatId, TelegramLongPollingSessionBot tgBot) {
    CommercialProject cp = new CommercialProject();
    cp.setNameAndSphere(update.getMessage().getText());
    bindingCommercial.clear();
    bindingCommercial.put(Binding.builder().type(COMMERCIAL).iter(1).chatId(chatId).build(), cp);
    bindingCommercial.keySet().forEach(System.out::println); //
    Stream.of(bindingCommercial.values()).forEach(System.out::println); //
    executeWrapper(SendMessage.builder().text("Что требуется разработать:").chatId(chatId).build(), tgBot);
    return bindingCommercial;
  }

  public Map<Binding, CommercialProject> handleProject(Update update, Map<Binding, CommercialProject> bindingCommercial,
      Long chatId, TelegramLongPollingSessionBot tgBot) {
    CommercialProject j = bindingCommercial.get(bindingCommercial.keySet().stream().findFirst().orElseThrow());
    j.setNeed(update.getMessage().getText());
    bindingCommercial.clear();
    bindingCommercial.put(Binding.builder().type(COMMERCIAL).chatId(chatId).iter(2).build(), j);
    bindingCommercial.keySet().forEach(System.out::println); //
    Stream.of(bindingCommercial.values()).forEach(System.out::println); //
    executeWrapper(SendMessage.builder().text("Ваше имя:").chatId(chatId).build(), tgBot);
    return bindingCommercial;
  }

  public Map<Binding, CommercialProject> handleName(Update update, Map<Binding, CommercialProject> bindingCommercial,
      Long chatId, TelegramLongPollingSessionBot tgBot) {
    CommercialProject j = bindingCommercial.get(bindingCommercial.keySet().stream().findFirst().orElseThrow());
    j.setName(update.getMessage().getText());
    bindingCommercial.clear();
    bindingCommercial.put(Binding.builder().type(COMMERCIAL).chatId(chatId).iter(3).build(), j);
    bindingCommercial.keySet().forEach(System.out::println); //
    Stream.of(bindingCommercial.values()).forEach(System.out::println); //
    executeWrapper(SendMessage.builder().text("Контакт для связи:").chatId(chatId).build(), tgBot);
    return bindingCommercial;
  }

  public Map<Binding, CommercialProject> handleContact(Update update, Map<Binding, CommercialProject> bindingCommercial,
      Long chatId, TelegramLongPollingSessionBot tgBot) {
    CommercialProject j = bindingCommercial.get(bindingCommercial.keySet().stream().findFirst().orElseThrow());
    j.setContact(update.getMessage().getText());
    bindingCommercial.clear();
    bindingCommercial.put(Binding.builder().type(COMMERCIAL).chatId(chatId).iter(4).build(), j);
    sendData(bindingCommercial, tgBot);
    bindingCommercial.keySet().forEach(System.out::println); //
    Stream.of(bindingCommercial.values()).forEach(System.out::println); //
    executeWrapper(SendMessage.builder()
        .text("Спасибо, " + bindingCommercial.get(bindingCommercial.keySet().stream().findFirst().orElseThrow()).getName()
            + "! В ближайшее время мы с вами свяжемся (контакт для связи: "
            + bindingCommercial.get(bindingCommercial.keySet().stream().findFirst().orElseThrow()).getContact() + ")")
        .chatId(chatId).build(), tgBot);
    bindingCommercial.clear();
    return bindingCommercial;
  }

  private void sendData(Map<Binding, CommercialProject> bindingCommercial, TelegramLongPollingSessionBot tgBot) {
    SendMessage sendData = SendMessage.builder()
        .chatId(joinChatId)
        .messageThreadId(joinThreadId)
        .text("Новая заявка на коммерческую разработку: " + bindingCommercial.get(bindingCommercial.keySet().stream().findFirst().orElseThrow()))
        .build();
    executeWrapper(sendData, tgBot);
  }
}
