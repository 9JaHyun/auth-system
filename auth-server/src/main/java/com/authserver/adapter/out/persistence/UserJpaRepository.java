package com.authserver.adapter.out.persistence;

import com.authserver.adapter.out.persistence.entity.UserJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByUsername(String username);
}