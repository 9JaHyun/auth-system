package com.userservice.application.port.out.persistence;

import com.userservice.domain.User;

public interface UserCommandPort {

    void save(User user);

    void update(User user);
}
