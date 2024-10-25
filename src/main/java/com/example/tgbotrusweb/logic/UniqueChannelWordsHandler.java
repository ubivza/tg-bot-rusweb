package com.example.tgbotrusweb.logic;

import static com.example.tgbotrusweb.utils.FileDataDownloader.*;

import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.logic.enums.Channels;
import com.example.tgbotrusweb.logic.interfaces.Handler;
import com.example.tgbotrusweb.service.CommentRemover;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UniqueChannelWordsHandler extends Handler {
  private final CommentRemover commentRemover;
  private final List<String> germanWords = readFromGermanFile();
  private final List<String> englishWords = readFromEnglishFile();
  private final List<String> italianWords = readFromItalianFile();
  private final List<String> frenchWords = readFromFrenchFile();

  @Override
  public void handleUpdate(Comment comment) {
    //TODO
    if (comment.getChannel() == Channels.GERMAN) {
      for (String s : germanWords) {
        String commentTextLowerCase = comment.getUpdate().getMessage().getText().toLowerCase();
        if (commentTextLowerCase.contains(s.toLowerCase())) {
          log.info("Comment contains link and german word: " + s);
          commentRemover.handle(comment);
        }
      }
    }
    if (comment.getChannel() == Channels.FRENCH) {
      for (String s : frenchWords) {
        String commentTextLowerCase = comment.getUpdate().getMessage().getText().toLowerCase();
        if (commentTextLowerCase.contains(s.toLowerCase())) {
          log.info("Comment contains link and french word: " + s);
          commentRemover.handle(comment);
        }
      }
    }
    if (comment.getChannel() == Channels.ENGLISH) {
      for (String s : englishWords) {
        String commentTextLowerCase = comment.getUpdate().getMessage().getText().toLowerCase();
        if (commentTextLowerCase.contains(s.toLowerCase())) {
          log.info("Comment contains link and english word: " + s);
          commentRemover.handle(comment);
        }
      }
    }
    if (comment.getChannel() == Channels.ITALY) {
      for (String s : italianWords) {
        String commentTextLowerCase = comment.getUpdate().getMessage().getText().toLowerCase();
        if (commentTextLowerCase.contains(s.toLowerCase())) {
          log.info("Comment contains link and italian word: " + s);
          commentRemover.handle(comment);
        }
      }
    }
  }
}
