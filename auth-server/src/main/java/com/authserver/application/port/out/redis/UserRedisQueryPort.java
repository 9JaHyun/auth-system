package com.authserver.application.port.out.redis;

import com.authserver.domain.User;

public interface UserRedisQueryPort {

    User getCachedUser(String username);
}
