package com.example.tgbotrusweb.keyboard;

import static com.example.tgbotrusweb.keyboard.enums.Navigation.*;

import com.example.tgbotrusweb.keyboard.enums.Navigation;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class NavigationInlineMarkup {
  public SendMessage createNavigationKeyboard(long chatId) {
    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setText("Выберите вариант ниже \uD83D\uDC47");

    InlineKeyboardMarkup navigationMarkup = new InlineKeyboardMarkup();

    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

    List<InlineKeyboardButton> rowInline1 = new ArrayList<>();

    InlineKeyboardButton button1 = InlineKeyboardButton.builder()
        .text(ABOUT_US.getName())
        .callbackData(ABOUT_US.getName())
        .build();
    InlineKeyboardButton button2 = InlineKeyboardButton.builder()
        .text(JOIN.getName())
        .callbackData(JOIN.getName())
        .build();

    rowInline1.add(button1);
    rowInline1.add(button2);

    rowsInline.add(rowInline1);

    List<InlineKeyboardButton> rowInline2 = new ArrayList<>();

    InlineKeyboardButton button3 = InlineKeyboardButton.builder()
        .text(NON_COMMERCIAL.getName())
        .callbackData(NON_COMMERCIAL.getName())
        .build();
    InlineKeyboardButton button4 = InlineKeyboardButton.builder()
        .text(COMMERCIAL.getName())
        .callbackData(COMMERCIAL.getName())
        .build();

    rowInline2.add(button3);
    rowInline2.add(button4);

    rowsInline.add(rowInline2);

    navigationMarkup.setKeyboard(rowsInline);
    message.setReplyMarkup(navigationMarkup);
    return message;
  }
}
