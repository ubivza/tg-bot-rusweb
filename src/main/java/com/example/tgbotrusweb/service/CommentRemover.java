package com.example.tgbotrusweb.service;

import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.logic.enums.Channels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class CommentRemover {

  public void handle(Comment comment) {
    log.info("I'm sending message to admin group, deleting comment and banning sender");
    String userName = comment.getUpdate().getMessage().getFrom().getUserName();
    long id = comment.getUpdate().getMessage().getFrom().getId();
    String message = "Comment content:"
        + System.lineSeparator() + comment.getUpdate().getMessage().getText()
        + System.lineSeparator() + "Comment written by: @" + userName
        + System.lineSeparator() + "Id: " + id
        + System.lineSeparator() + "In channel: " + comment.getChannel().name();
    SendMessage sendMessageRequest = new SendMessage(String.valueOf(Channels.ADMIN.getId()), message);
    try {
      comment.getClient().execute(sendMessageRequest);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }

    String chatId = String.valueOf(comment.getChannel().getId());
    DeleteMessage deleteMessageRequest = new DeleteMessage(chatId,
        comment.getUpdate().getMessage().getMessageId());
    try {
      comment.getClient().execute(deleteMessageRequest);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }

    BanChatMember banChatMember = new BanChatMember(chatId, comment.getUpdate().getMessage().getFrom().getId());
    banChatMember.setRevokeMessages(true);
    try {
      comment.getClient().execute(banChatMember);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }
}
