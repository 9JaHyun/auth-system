package com.authserver.adapter.out.persistence;

import com.authserver.adapter.out.persistence.entity.UserJpaEntity;
import com.authserver.application.port.out.persistence.UserPersistenceQueryPort;
import com.authserver.domain.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class UserPersistenceAdapter implements UserPersistenceQueryPort {

    private final UserJpaRepository userJpaRepository;
    private final UserEntityMapper userEntityMapper;

    public UserPersistenceAdapter(UserJpaRepository userJpaRepository,
        UserEntityMapper userEntityMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public User getUser(String username) {
        UserJpaEntity userJpaEntity = userJpaRepository.findByUsername(username)
            .orElseThrow(EntityNotFoundException::new);

        return userEntityMapper.mapToDomainEntity(userJpaEntity);
    }
}
