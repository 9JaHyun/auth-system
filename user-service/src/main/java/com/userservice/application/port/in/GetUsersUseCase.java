package com.userservice.application.port.in;

import com.userservice.domain.User;
import java.util.List;

public interface GetUsersUseCase {

    List<User> getUsersInfo();
}
