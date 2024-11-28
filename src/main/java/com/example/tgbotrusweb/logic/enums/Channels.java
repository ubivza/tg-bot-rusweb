package com.example.tgbotrusweb.logic.enums;

import java.util.ResourceBundle;
import lombok.Getter;

@Getter
public enum Channels {
  ITALY,
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

  private final ResourceBundle BUNDLE = ResourceBundle.getBundle("chantest");

  public static Channels getById(Long id) {
    for (Channels channel : Channels.values()) {
      if (channel.getId().equals(id)) {
        return channel;
      }
    }
    throw new IllegalArgumentException("No channel found for ID: " + id);
  }
}
