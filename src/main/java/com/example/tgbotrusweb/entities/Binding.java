package com.example.tgbotrusweb.entities;

import com.example.tgbotrusweb.keyboard.enums.Navigation;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Binding {
  private Long chatId;
  private Navigation type;
  private Integer iter;
}
