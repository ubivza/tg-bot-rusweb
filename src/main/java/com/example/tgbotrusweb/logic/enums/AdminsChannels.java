package com.example.tgbotrusweb.logic.enums;

import java.util.Arrays;
import java.util.Optional;
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

  private final ResourceBundle BUNDLE = ResourceBundle.getBundle("channels");

  public static Optional<AdminsChannels> getAdminsChannelById(Integer id) {
    return Arrays.stream(AdminsChannels.values())
        .filter(x -> id.equals(x.getId()))
        .findAny();
  }
}