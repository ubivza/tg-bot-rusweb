package com.example.tgbotrusweb.integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Getter
public class CommentResponse {
    @JsonProperty("is_spam")
    private String isSpam;
}
