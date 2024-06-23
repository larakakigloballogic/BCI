package com.bci.exercise.service;

import com.bci.exercise.config.security.JWTUtil;
import com.bci.exercise.model.UserEntity;
import com.bci.exercise.model.user.RequestUser;
import com.bci.exercise.model.user.User;
import com.bci.exercise.repository.PhoneRepository;
import com.bci.exercise.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Log4j2
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private RequestUser requestUser;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Fetching user details for {}", username);
        final UserEntity userEntity = userRepository.findUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found."));
        final User user = modelMapper.map(userEntity, User.class);
        requestUser.setUser(user);
        return user;
    }

}
