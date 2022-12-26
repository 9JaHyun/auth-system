package com.userservice.domain;

public record User(
    Long id,
    String username,
    String password,
    String nickname,
    String email,
    UserStatus userStatus
) {

    public static User createUser(String username, String password, String nickname, String email) {
        return new User(null, username, password, nickname, email, UserStatus.DEACTIVATED);
    }

    public User activate() {
        return new User(this.id, this.username, this.password, this.nickname, this.email, UserStatus.ACTIVATED);
    }
}
