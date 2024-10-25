package com.example.tgbotrusweb.logic;

import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.logic.interfaces.Handler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LinksHandler extends Handler {
  private static final String HTTP = "http";
  private static final String HTTPS = "https";
  private static final String regExURLContainsHTTPOrHTTPS =
      "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";
  private static final String regexURLWithoutProtocol =
      "[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";

  @Override
  public void handleUpdate(Comment comment) {
    //TODO
    //do like this
    String[] splittedCommentText = comment.getUpdate().getMessage().getText().split(" ");
    for (String s : splittedCommentText) {
      if (isALink(s)) {
        log.info("Got comment with link: " + s);
        next.handleUpdate(comment);
      }
    }

    //or like this
    if (comment.getUpdate().getMessage().getText().contains(HTTP)) {

    }

    //or use regEx
  }

  private boolean isALink(String text) {
    //check if text is a link
    return text.equals("https");
  }
}
