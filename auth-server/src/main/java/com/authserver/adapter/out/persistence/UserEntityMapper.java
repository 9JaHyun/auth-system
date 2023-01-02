package com.authserver.adapter.out.persistence;

import com.authserver.adapter.out.persistence.entity.UserJpaEntity;
import com.authserver.domain.User;
import org.springframework.stereotype.Component;

@Component
class UserEntityMapper {

    User mapToDomainEntity(UserJpaEntity jpaEntity) {
        return new User(jpaEntity.getId(), jpaEntity.getUsername(), jpaEntity.getPassword(),
            jpaEntity.getNickName(), jpaEntity.getEmail(), jpaEntity.getStatus());
    }

    UserJpaEntity mapToDomainEntity(User user) {
        return new UserJpaEntity(user.id(), user.username(), user.password(), user.nickname(),
            user.email(), user.userStatus());
    }

}
