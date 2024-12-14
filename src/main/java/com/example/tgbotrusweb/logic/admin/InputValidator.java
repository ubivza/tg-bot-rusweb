package com.example.tgbotrusweb.logic.admin;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class InputValidator {

  private static final String VALID_WORDS_REGEX_ADD = "^/add\\s([\\p{L}\\d_'\\s]+;?)+$";
  private static final String VALID_WORD_SHOW = "/show";


  /**
   * Проверяет, соответствует ли входной текст заданному формату.
   *
   * @param input Входной текст
   * @return true, если формат корректен, иначе false
   */
  public boolean isValidWordsInput(String input) {
    return Pattern.compile(VALID_WORDS_REGEX_ADD).matcher(input).matches() || input.equals(VALID_WORD_SHOW);
  }
}
