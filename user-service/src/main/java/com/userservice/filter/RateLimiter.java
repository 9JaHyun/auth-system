package com.userservice.filter;

import io.netty.util.internal.ObjectUtil;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RateLimiter {
    private static AtomicIntegerFieldUpdater<RateLimiter> bucketLevelUpdator
        = AtomicIntegerFieldUpdater.newUpdater(RateLimiter.class, "bucketLevel");

    private static AtomicLongFieldUpdater<RateLimiter> lastRefillTimeNanosUpdator
        = AtomicLongFieldUpdater.newUpdater(RateLimiter.class, "lastRefillTimeNanos");

    private final int bucketSize;
    private final int refillAmount;
    private final Long refillIntervalNanos;
    private volatile int bucketLevel;
    private volatile long lastRefillTimeNanos;

    public RateLimiter(int bucketSize, TimeUnit refillIntervalUnit, int refillAmount, Long refillInterval) {
        this.bucketSize = ObjectUtil.checkPositive(bucketSize, "bucketSize");
        this.refillAmount = ObjectUtil.checkPositive(refillAmount, "refillAmount");
        refillIntervalNanos = refillIntervalUnit.toNanos(
            ObjectUtil.checkPositive(refillInterval, "refillAmount")
        );
        bucketLevel = bucketSize;
        lastRefillTimeNanos = System.nanoTime();
    }

    public boolean canAcquire() {
        refillTokens();

        for (; ; ) {
            final int oldBucketLevel = bucketLevel;
            final int newBucketLevel = oldBucketLevel - 1;

            log.info("oldBucketLevel is {}", oldBucketLevel);
            log.info("newBucketLevel is {}", newBucketLevel);


            if (oldBucketLevel == 0) {
                return false;
            }
            if (newBucketLevel < 0) {
                return false;
            }

            if (!bucketLevelUpdator.compareAndSet(this, oldBucketLevel, newBucketLevel)) {
                continue;
            }

            return true;
        }
    }

    private void refillTokens() {
        log.info("refill!");
        for (; ; ) {
            final long currentTimeNanos = System.nanoTime();
            final long lastRefillTimeNanos = this.lastRefillTimeNanos; // access 마다 메모리에서 가져오면 비효율적
            final long elapsedTimeNanos = currentTimeNanos - lastRefillTimeNanos;
            if (elapsedTimeNanos < refillIntervalNanos) {
                break;
            }

            final long actualRefillAmount = (elapsedTimeNanos / refillIntervalNanos) * refillAmount;
            final int oldBucketLevel = bucketLevel;
            final int newBucketLevel;
            final long numRefills = elapsedTimeNanos / refillIntervalNanos;


            if (oldBucketLevel + actualRefillAmount > bucketSize) {
                newBucketLevel = bucketSize;
            } else {
                newBucketLevel = (int) (oldBucketLevel + actualRefillAmount);
            }
            if (bucketLevelUpdator.compareAndSet(this, oldBucketLevel, newBucketLevel)) {
                lastRefillTimeNanosUpdator.addAndGet(this, numRefills * refillIntervalNanos);
                break;
            }
        }
    }
}
