package com.example.tgbotrusweb.logic;

import com.example.tgbotrusweb.exception.LLMRequestsLimitException;
import com.example.tgbotrusweb.exception.SomethingWentWrongException;
import com.example.tgbotrusweb.integration.PythonBackendClient;
import com.example.tgbotrusweb.logic.domain.Comment;
import com.example.tgbotrusweb.service.CommentRemover;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
@RequiredArgsConstructor
public class NaiveCheckPassedHandler {
    private final CommentRemover commentRemover;
    private final PythonBackendClient client;
    private final ReentrantLock lock = new ReentrantLock();
    private Instant timerStartTime;
    private static final long TWENTY_FOUR_HOURS_SECONDS = 24 * 60 * 60;
    private final AtomicReference<Boolean> flag = new AtomicReference<>(true);

    public void handleUpdate(Comment comment) {
        checkTimerAndUpdateFlag();
        try {
            if (flag.get()) {
                Optional<String> isSpam = client.validateComment(comment.getUpdate().getMessage().getText());

                if (isSpam.isEmpty()) {
                    throw new SomethingWentWrongException("isSpam empty");
                }

                if (isSpam.get().equals("spam")) {
                    commentRemover.handle(comment);
                } else if (isSpam.get().equals("not_spam")) {
                    log.info("AI said not spam");
                } else {
                    throw new SomethingWentWrongException("isSpam with status " + isSpam.get());
                }
            }
        } catch (LLMRequestsLimitException exception) {
            lock.lock();
            try {
                flag.compareAndSet(true, false);
                timerStartTime = Instant.now();
                log.info("Timer start {}", timerStartTime);
            } finally {
                lock.unlock();
            }

            sendToMe(comment, exception);
        } catch (SomethingWentWrongException exception) {
            sendToMe(comment, exception);
        }
    }

    private void checkTimerAndUpdateFlag() {
        if (timerStartTime == null) {
            log.info("Таймер еще не запущен");
            return;
        }

        Duration elapsedTime = Duration.between(timerStartTime, Instant.now());
        long elapsedSeconds = elapsedTime.getSeconds();

        log.info("Прошло времени: " + elapsedSeconds + " секунд");

        if (elapsedSeconds >= TWENTY_FOUR_HOURS_SECONDS) {
            lock.lock();
            try {
                flag.compareAndSet(false, true);
                timerStartTime = null;
            } finally {
                lock.unlock();
            }
        } else {
            log.info("До 24 часов осталось: {} секунд", (TWENTY_FOUR_HOURS_SECONDS - elapsedSeconds));
        }
    }

    private void sendToMe(Comment comment, Exception exception) {
        SendMessage sendMessageRequest = new SendMessage("-100626688374", exception.getMessage());
        try {
            comment.getClient().execute(sendMessageRequest);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
