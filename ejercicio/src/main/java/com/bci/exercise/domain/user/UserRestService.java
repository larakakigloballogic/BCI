package com.bci.exercise.domain.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserRestService extends UserDetailsService {

    UserDetails getUserDetails(String username, String token) throws UsernameNotFoundException;

    User createUser(User user);

    User login();
}
