package com.example.tgbotrusweb.integration.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentRequest {
    private String comment;
}
