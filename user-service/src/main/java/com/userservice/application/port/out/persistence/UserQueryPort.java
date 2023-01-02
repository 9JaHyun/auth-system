package com.userservice.application.port.out.persistence;

import com.userservice.domain.User;
import java.util.List;

public interface UserQueryPort {

    User getById(Long id);

    User getByUsername(String username);

    boolean isExistByUsername(String username);

    boolean isExistByNickname(String nickname);

    boolean isExistByEmail(String email);

    List<User> getAllUser();

}
