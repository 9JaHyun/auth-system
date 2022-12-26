package com.userservice.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;

public class BaseEntity<T> {

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        lastModifiedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        lastModifiedAt = LocalDateTime.now();
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    @SuppressWarnings("unchecked")
    public T apply(final LocalDateTime now) {
        this.createdAt = now;
        this.lastModifiedAt = now;
        return (T) this;
    }
}
