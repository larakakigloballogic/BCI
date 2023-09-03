package com.bci.exercise.infrastructure.repositories.user;

import com.bci.exercise.domain.error.NotFoundException;
import com.bci.exercise.domain.user.User;
import com.bci.exercise.domain.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    @Override
    public User findUserByEmail(String username) throws UsernameNotFoundException {
        log.info("Fetching user details for {}", username);
        UserEntity userEntity = userRepository.findUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found."));
        return modelMapper.map(userEntity, User.class);
    }

    @Override
    public User createUser(User user) {
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        UserEntity saved = userRepository.save(userEntity);
        Optional.ofNullable(userEntity.getPhones())
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .forEach(p -> p.setUser(saved));
        phoneRepository.saveAll(userEntity.getPhones());
        return modelMapper.map(saved, User.class);
    }

    @Override
    public User updateUser(User user){
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(() -> new NotFoundException("Usuario inexistente"));
        userEntity.setLastLogin(LocalDateTime.now());
        userEntity.setToken(user.getToken());
        userRepository.save(userEntity);
        return modelMapper.map(userEntity, User.class);
    }
}
