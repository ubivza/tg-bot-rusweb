package com.example.tgbotrusweb.logic;

import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.logic.interfaces.Handler;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LinksHandler extends Handler {
  private static final String regExURLContainsHTTPOrHTTPS =
      "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";
  private static final String regexURLWithoutProtocol =
      "[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";

  @Override
  public void handleUpdate(Comment comment) {
    String commentText = comment.getUpdate().getMessage().getText();
    if (Pattern.compile(regExURLContainsHTTPOrHTTPS).matcher(commentText).find()) {
      log.info("Got comment with link: " + commentText);
      next.handleUpdate(comment);
    }
  }
}
