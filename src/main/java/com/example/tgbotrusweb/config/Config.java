package com.example.tgbotrusweb.config;

import com.example.tgbotrusweb.logic.GeneralWordsHandler;
import com.example.tgbotrusweb.logic.LinksHandler;
import com.example.tgbotrusweb.logic.ReplyHandler;
import com.example.tgbotrusweb.logic.UniqueChannelWordsHandler;
import com.example.tgbotrusweb.service.CommentRemover;
import com.example.tgbotrusweb.service.RulesCommentWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class Config {

  private final CommentRemover commentRemover;

  private final RulesCommentWriter commentWriter;

  @Bean
  public ReplyHandler replyHandler() {
    ReplyHandler replyHandler = new ReplyHandler(commentWriter);
    replyHandler.setNext(linksHandler());
    return replyHandler;
  }

  @Bean
  public LinksHandler linksHandler() {
    LinksHandler linksHandler = new LinksHandler();
    linksHandler.setNext(generalWordsHandler());
    return linksHandler;
  }

  @Bean
  public GeneralWordsHandler generalWordsHandler() {
    GeneralWordsHandler generalWordsHandler = new GeneralWordsHandler(commentRemover);
    generalWordsHandler.setNext(uniqueChannelWordsHandler());
    return generalWordsHandler;
  }

  @Bean
  public UniqueChannelWordsHandler uniqueChannelWordsHandler() {
    return new UniqueChannelWordsHandler(commentRemover);
  }
}
