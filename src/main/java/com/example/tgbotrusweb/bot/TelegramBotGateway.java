package com.example.tgbotrusweb.bot;

import com.example.tgbotrusweb.bot.model.TelegramBotModel;
import com.example.tgbotrusweb.handler.CommercialHandler;
import com.example.tgbotrusweb.handler.DefaultHandler;
import com.example.tgbotrusweb.handler.JoinerHandler;
import com.example.tgbotrusweb.handler.NonCommercialHandler;
import com.example.tgbotrusweb.keyboard.NavigationInlineMarkup;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;

@Component
@Slf4j
public class TelegramBotGateway extends TelegramLongPollingSessionBot {
  private final TelegramBotModel tgBot;
  private final NavigationInlineMarkup inlineMarkup;
  private final JoinerHandler joinerHandler;
  private final CommercialHandler commercialHandler;
  private final NonCommercialHandler nonCommercialHandler;
  private final DefaultHandler handler;

  public TelegramBotGateway(TelegramBotModel tgBot, NavigationInlineMarkup inlineMarkup, CommercialHandler commercialHandler,
      NonCommercialHandler nonCommercialHandler, JoinerHandler joinerHandler) {
    this.tgBot = tgBot;
    this.inlineMarkup = inlineMarkup;
    this.commercialHandler = commercialHandler;
    this.nonCommercialHandler = nonCommercialHandler;
    handler = new DefaultHandler(inlineMarkup, joinerHandler, commercialHandler, nonCommercialHandler);
    this.joinerHandler = joinerHandler;
  }

  @Override
  public String getBotUsername() {
    return tgBot.getName();
  }
  @Override
  public String getBotToken() {
    return tgBot.getToken();
  }

  @Override
  public void onUpdateReceived(Update update, Optional<Session> botSession) {
    log.info(update + "");
    log.info(botSession + "");
    handler.handle(update, this);
  }
}
