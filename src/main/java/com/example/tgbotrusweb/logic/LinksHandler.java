package com.example.tgbotrusweb.logic;

import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.logic.interfaces.Handler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class LinksHandler extends Handler {
  private final NaiveCheckPassedHandler naiveCheckPassedHandler;
  private static final String regExURLContainsHTTPOrHTTPS =
      "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";
  private static final String USERNAME = "mention";
  private static final String URL = "url";
  private static final String TEXT_LINK = "text_link";
  private static final String ADMIN_USERNAME = "@admin";
  private static final String ADMIN_USERNAME2 = "@administrator";

  @Override
  public void handleUpdate(Comment comment) {
    String commentText = comment.getUpdate().getMessage().getText();
    if (commentText == null || commentText.isEmpty()) {
      log.info("Got comment without text (sticker maybe), skip");
      return;
    }

    List<MessageEntity> commentContainsURLIfNotEmpty = new ArrayList<>();
    if (comment.getUpdate().getMessage().getEntities() != null) {
      commentContainsURLIfNotEmpty = comment.getUpdate().getMessage().getEntities().stream()
          .filter(x -> (x.getType().equals(USERNAME) && (!x.getText().equals(ADMIN_USERNAME) && !x.getText().equals(ADMIN_USERNAME2)))
              || x.getType().equals(URL)
              || x.getType().equals(TEXT_LINK))
          .toList();
    }

    if (Pattern.compile(regExURLContainsHTTPOrHTTPS).matcher(commentText).find() || !commentContainsURLIfNotEmpty.isEmpty()) {
      log.info("Got comment with link: " + commentText);
      next.handleUpdate(comment);
    } else {
        naiveCheckPassedHandler.handleUpdate(comment);
    }
  }
}
