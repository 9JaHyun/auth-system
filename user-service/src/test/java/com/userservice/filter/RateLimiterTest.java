package com.userservice.filter;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RateLimiterTest {

    @DisplayName("bucket 안의 token을 모두 소진 시 실행에 실패한다.")
    @Test
    void isAcquire_fail() {
        // given
        RateLimiter rateLimiter = new RateLimiter(10, TimeUnit.MILLISECONDS, 1, 1000L);

        // when
        // bucket 모두 소진 시 실패
        for (int i = 0; i < 10; i++) {
            rateLimiter.canAcquire();
        }

        // then
        assertFalse(rateLimiter.canAcquire());
    }

    @DisplayName("token이 남아있다면 실행에 성공한다.")
    @Test
    void isAcquire_success() throws InterruptedException {
        // given
        RateLimiter rateLimiter = new RateLimiter(10, TimeUnit.MILLISECONDS, 10, 1000L);

        // when
        // bucket 모두 소진 시 실패
        for (int i = 0; i < 10; i++) {
            sleep(1000L);
            rateLimiter.canAcquire();
        }

        // then
        assertTrue(rateLimiter.canAcquire());
    }
}