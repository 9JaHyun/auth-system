package com.userservice.adapter.out.persistence;

import com.userservice.adapter.out.persistence.entity.UserJpaEntity;
import com.userservice.application.port.out.persistence.UserCommandPort;
import com.userservice.application.port.out.persistence.UserQueryPort;
import com.userservice.domain.User;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceAdapter implements UserCommandPort, UserQueryPort {

    private final UserJpaRepository userRepository;

    public UserPersistenceAdapter(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user) {
        UserJpaEntity entity = new UserJpaEntity(user.username(), user.password(), user.nickname(), user.email(), user.userStatus());
        userRepository.save(entity);
    }

    @Override
    public void update(User user) {
        UserJpaEntity entity = new UserJpaEntity(user.id(), user.username(), user.password(), user.nickname(), user.email(), user.userStatus());
        userRepository.save(entity);
    }

    @Override
    public User getById(Long id) {
        UserJpaEntity userEntity = userRepository.findById(id).orElseThrow();
        return new User(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword(), userEntity.getNickname(), userEntity.getEmail(), userEntity.getStatus());
    }

    @Override
    public User getByUsername(String username) {
        UserJpaEntity userEntity = userRepository.findByUsername(username).orElseThrow(
            () -> new AssertionError("no user data exist")
        );
        return new User(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword(), userEntity.getNickname(), userEntity.getEmail(), userEntity.getStatus());
    }

    @Override
    public boolean isExistByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean isExistByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean isExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<User> getAllUser() {
        List<UserJpaEntity> userJpaEntities = userRepository.findAll();
        return userJpaEntities.stream()
            .map(e -> new User(e.getId(), e.getUsername(), e.getPassword(), e.getNickname(),
                e.getEmail(), e.getStatus()))
            .toList();
    }
}
