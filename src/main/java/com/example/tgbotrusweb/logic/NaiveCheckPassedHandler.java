package com.example.tgbotrusweb.logic;

import com.example.tgbotrusweb.integration.PythonBackendClient;
import com.example.tgbotrusweb.integration.limiter.DailyQuotaLimiter;
import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.service.CommentRemover;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
@RequiredArgsConstructor
public class NaiveCheckPassedHandler {
    private final DailyQuotaLimiter limiter;
    private final CommentRemover commentRemover;
    private final PythonBackendClient client;
    private final AtomicBoolean notified = new AtomicBoolean(false);

    public void handleUpdate(Comment comment) {
//        if (!limiter.tryAcquire()) {
//            if (notified.compareAndSet(false, true)) {
//                sendToMe(comment, new Exception("Daily LLM quota exhausted"));
//            }
//            log.info("Daily LLM quota exhausted");
//            return;
//        }
//        notified.compareAndSet(true, false);

        client.validateComment(comment)
                .ifPresent(validationResult -> {
                    if (validationResult.equals("spam")) {
                        log.info("AI marked as spam: {}", comment.getUpdate().getMessage().getText());
                        commentRemover.handle(comment);
                    } else if (validationResult.equals("not_spam")) {
                        log.info("AI marked as not_spam: {}", comment.getUpdate().getMessage().getText());
                    } else {
                        log.warn("AI response: {}", validationResult);
                    }
                });
    }
}
