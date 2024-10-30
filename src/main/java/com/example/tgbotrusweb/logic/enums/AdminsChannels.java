package com.example.tgbotrusweb.logic.enums;

import java.util.ResourceBundle;
import lombok.Getter;

@Getter
public enum AdminsChannels {
  ITALY,
  FRENCH,
  GERMAN,
  ENGLISH;

  public final int id;

  AdminsChannels() {
    this.id = Integer.parseInt(BUNDLE.getString("Thread." + name()));
  }

  private final ResourceBundle BUNDLE = ResourceBundle.getBundle("channels");
}