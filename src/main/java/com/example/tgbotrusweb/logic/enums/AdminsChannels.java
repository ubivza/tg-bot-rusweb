package com.example.tgbotrusweb.logic.enums;

import java.util.ResourceBundle;
import lombok.Getter;

@Getter
public enum AdminsChannels {
  ITALY,
  FRENCH,
  GERMAN,
  ENGLISH,
  SPANISH,
  GENERAL;

  public final int id;

  AdminsChannels() {
    this.id = Integer.parseInt(BUNDLE.getString("Thread." + name()));
  }

  private final ResourceBundle BUNDLE = ResourceBundle.getBundle("chantest");

  public static AdminsChannels getById(int id) {
    for (AdminsChannels channel : AdminsChannels.values()) {
      if (channel.getId()==id) {
        return channel;
      }
    }
    throw new IllegalArgumentException("No channel found for ID: " + id);
  }
}