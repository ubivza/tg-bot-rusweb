package com.example.tgbotrusweb.handler;

import static com.example.tgbotrusweb.keyboard.enums.Navigation.JOIN;

import com.example.tgbotrusweb.entities.Binding;
import com.example.tgbotrusweb.entities.Joiner;
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
public class JoinerHandler {
 private final String joinChatId = "-1001990644808";
 private final int joinThreadId = 5;
 public Map<Binding, Joiner> handleName(Update update, Map<Binding, Joiner> bindingJoiner,
     long chatId, TelegramLongPollingSessionBot tgBot) {
  Joiner j = new Joiner();
  j.setName(update.getMessage().getText());
  bindingJoiner.clear();
  bindingJoiner.put(Binding.builder().type(JOIN).chatId(chatId).iter(1).build(), j);
  bindingJoiner.keySet().forEach(System.out::println); //
  Stream.of(bindingJoiner.values()).forEach(System.out::println); //
  executeWrapper(SendMessage.builder().text("Ваша специализация и стек технологий:").chatId(chatId).build(), tgBot);
  return bindingJoiner;
 }
 public Map<Binding, Joiner> handleSpec(Update update, Map<Binding, Joiner> bindingJoiner,
     long chatId, TelegramLongPollingSessionBot tgBot) {
  Joiner j = bindingJoiner.get(bindingJoiner.keySet().stream().findFirst().orElseThrow());
  j.setSpecialization(update.getMessage().getText());
  bindingJoiner.clear();
  bindingJoiner.put(Binding.builder().type(JOIN).chatId(chatId).iter(2).build(), j);
  bindingJoiner.keySet().forEach(System.out::println); //
  Stream.of(bindingJoiner.values()).forEach(System.out::println); //
  executeWrapper(SendMessage.builder().text("Контакт для связи:").chatId(chatId).build(), tgBot);
  return bindingJoiner;
 }
 public Map<Binding, Joiner> handleContact(Update update, Map<Binding, Joiner> bindingJoiner,
     long chatId, TelegramLongPollingSessionBot tgBot) {
  Joiner j = bindingJoiner.get(bindingJoiner.keySet().stream().findFirst().orElseThrow());
  j.setContact(update.getMessage().getText());
  bindingJoiner.clear();
  bindingJoiner.put(Binding.builder().type(JOIN).chatId(chatId).iter(3).build(), j);
  sendData(bindingJoiner, tgBot);
  bindingJoiner.keySet().forEach(System.out::println); //
  Stream.of(bindingJoiner.values()).forEach(System.out::println); //
  executeWrapper(SendMessage.builder()
      .text("Спасибо, " + bindingJoiner.get(bindingJoiner.keySet().stream().findFirst().orElseThrow()).getName()
          + "! В ближайшее время мы с вами свяжемся (контакт для связи: "
          + bindingJoiner.get(bindingJoiner.keySet().stream().findFirst().orElseThrow()).getContact() + ")")
      .chatId(chatId).build(), tgBot);
  bindingJoiner.clear();
  return bindingJoiner;
 }

 private void executeWrapper(SendMessage message, TelegramLongPollingSessionBot tgBot) {
  try {
   tgBot.execute(message);
  } catch (TelegramApiException e) {
   log.info("Exception while executing sendMessage");
   throw new RuntimeException(e);
  }
 }
 private void sendData(Map<Binding, Joiner> bindingJoiner, TelegramLongPollingSessionBot tgBot) {
  SendMessage sendData = SendMessage.builder()
      .chatId(joinChatId)
      .messageThreadId(joinThreadId)
      .text("Новая заявка на позицию разработчика: " + bindingJoiner.get(bindingJoiner.keySet().stream().findFirst().orElseThrow()))
      .build();
  executeWrapper(sendData, tgBot);
 }
}
