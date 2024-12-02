package com.example.tgbotrusweb.logic.enums;

import java.util.ResourceBundle;
import lombok.Getter;

@Getter
public enum Channels {
  ITALIAN,
  FRENCH,
  GERMAN,
  ENGLISH,
  ADMIN,
  SPANISH,
  TEST;

  public final Long id;

  Channels() {
    this.id = Long.valueOf(BUNDLE.getString("Channel." + name()));
  }

  private final ResourceBundle BUNDLE = ResourceBundle.getBundle("channels");
}
