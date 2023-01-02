package com.authserver.application.port.out.persistence;

import com.authserver.domain.User;
import org.springframework.stereotype.Component;

@Component
public interface UserPersistenceQueryPort {

    User getUser(String username);
}
