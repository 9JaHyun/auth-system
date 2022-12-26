package com.userservice.application.port.out.redis;

public interface UserRedisQueryPort {

    String getAuthCode(String username);
}
