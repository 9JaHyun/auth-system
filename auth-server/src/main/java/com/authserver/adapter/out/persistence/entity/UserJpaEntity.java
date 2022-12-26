package com.authserver.adapter.out.persistence.entity;

import com.authserver.domain.UserStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserJpaEntity extends BaseEntity<UserJpaEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    private String username;
    private String password;
    private String nickName;
    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    protected UserJpaEntity() {
        this.id = null;
    }

    public UserJpaEntity(String username, String password, String nickName, String email,
        UserStatus status) {
        this.id = null;
        this.username = username;
        this.password = password;
        this.nickName = nickName;
        this.email = email;
        this.status = status;
    }

    public UserJpaEntity(Long id, String username, String password, String nickName, String email,
        UserStatus status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickName = nickName;
        this.email = email;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickName() {
        return nickName;
    }

    public String getEmail() {
        return email;
    }

    public UserStatus getStatus() {
        return status;
    }
}
