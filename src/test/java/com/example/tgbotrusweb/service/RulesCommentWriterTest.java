package com.example.tgbotrusweb.service;

import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.logic.enums.Channels;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RulesCommentWriterTest {

    @Mock
    private TelegramClient telegramClient;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @InjectMocks
    private RulesCommentWriter rulesCommentWriter;

    private Comment comment;

    @BeforeEach
    void setUp() {
        when(update.getMessage()).thenReturn(message);
        when(message.getMessageId()).thenReturn(123);
    }

    @Test
    void writeRulesInComments_German() throws TelegramApiException {
        // Подготовка
        comment = Comment.builder()
                .channel(Channels.GERMAN)
                .update(update)
                .client(telegramClient)
                .build();

        // Выполнение
        rulesCommentWriter.writeRulesInComments(comment);

        // Проверка
        verify(telegramClient).execute(argThat((SendMessage sendMessage) -> {
            return sendMessage.getChatId().equals(String.valueOf(Channels.GERMAN.getId())) &&
                   sendMessage.getText().contains("Liebe Freunde") &&
                   sendMessage.getReplyToMessageId().equals(123);
        }));
    }

    @Test
    void writeRulesInComments_French2_WithLink() throws TelegramApiException {
        // Подготовка
        comment = Comment.builder()
                .channel(Channels.FRENCH2)
                .update(update)
                .client(telegramClient)
                .build();

        // Выполнение
        rulesCommentWriter.writeRulesInComments(comment);

        // Проверка
        verify(telegramClient).execute(argThat((SendMessage sendMessage) -> {
            return sendMessage.getChatId().equals(String.valueOf(Channels.FRENCH2.getId())) &&
                   sendMessage.getText().contains("LIEN") &&
                   !sendMessage.getEntities().isEmpty() &&
                   sendMessage.getEntities().stream()
                      .anyMatch(entity -> entity.getType().equals("text_link") &&
                              entity.getUrl().equals("https://t.me/c/2056162680/4662"));
        }));
    }

    @Test
    void handleTelegramApiException() throws TelegramApiException {
        // Подготовка
        comment = Comment.builder()
                .channel(Channels.GERMAN)
                .update(update)
                .client(telegramClient)
                .build();
        
        when(telegramClient.execute(any(SendMessage.class)))
                .thenThrow(new TelegramApiException("Test exception"));

        // Проверка
        assertThrows(RuntimeException.class, 
                () -> rulesCommentWriter.writeRulesInComments(comment));
    }
} 