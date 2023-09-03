package com.bci.exercise.domain.user;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

    User findUserByEmail(String username) throws UsernameNotFoundException;

    User createUser(User user);

    User updateUser(User user);
}
