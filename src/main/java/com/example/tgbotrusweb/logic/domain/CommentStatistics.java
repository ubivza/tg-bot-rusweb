package com.example.tgbotrusweb.logic.domain;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentStatistics {
    private AtomicInteger totalComments;
    private AtomicInteger totalSpamComments;
}
