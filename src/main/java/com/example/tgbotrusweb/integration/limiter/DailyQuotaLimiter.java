package com.example.tgbotrusweb.integration.limiter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

// думал что будет рейт лимит, его вроде нет, класс не нужен
@Component
public class DailyQuotaLimiter {
    @Value("${llm.usage.daily.limit}")
    private Integer DAILY_LIMIT;
    private final AtomicInteger counter = new AtomicInteger(0);
    private volatile LocalDate currentDay = LocalDate.now();
    private final ReentrantLock lock = new ReentrantLock();

    public boolean tryAcquire() {
        LocalDate today = LocalDate.now();

        if (!today.equals(currentDay)) {
            lock.lock();
            try {
                if (!today.equals(currentDay)) {
                    counter.set(0);
                    currentDay = today;
                }
            } finally {
                lock.unlock();
            }
        }

        return counter.incrementAndGet() <= DAILY_LIMIT;
    }
}
