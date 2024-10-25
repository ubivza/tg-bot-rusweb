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
    //TODO send text and user to ADMIN group
    SendMessage sendMessageRequest = new SendMessage(String.valueOf(Channels.ADMIN.getId()),
        "Comment content: " + comment.getUpdate().getMessage().getText() +
            " ; Comment written by: @" + comment.getUpdate().getMessage().getFrom().getUserName());
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

    //TODO Ban user that send comment
    BanChatMember banChatMember = new BanChatMember(chatId, comment.getUpdate().getMessage().getFrom().getId());
    try {
      comment.getClient().execute(banChatMember);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }
}
