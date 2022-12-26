package com.userservice.application.port.out.redis;

public interface UserRedisCommandPort {

    void saveAuthCode(String username, String authCode);
}
