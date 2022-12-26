package com.authserver.application.port.out.redis;

import com.authserver.domain.User;

public interface UserRedisCommandPort {

    void saveRecentSignInUser(User user);
}
