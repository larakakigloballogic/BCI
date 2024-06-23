package com.bci.exercise.service;

import com.bci.exercise.config.exception.NotFoundException;
import com.bci.exercise.config.exception.UserException;
import com.bci.exercise.config.security.JWTUtil;
import com.bci.exercise.dto.LoginResponseDTO;
import com.bci.exercise.dto.UserRequestDTO;
import com.bci.exercise.dto.UserResponseDTO;
import com.bci.exercise.model.UserEntity;
import com.bci.exercise.model.user.RequestUser;
import com.bci.exercise.model.user.User;
import com.bci.exercise.repository.PhoneRepository;
import com.bci.exercise.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    private JWTUtil jwtUtil;

    @Autowired
    private RequestUser requestUser;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO){
        log.info("creating user " + userRequestDTO);
        final Optional<UserEntity> userEntityOptional = userRepository.findUserByEmail(userRequestDTO.getEmail());
        if(userEntityOptional.isPresent()){
            throw new UserException("No se puede crear usuario debido a que ya existe.");
        }
        UserEntity created =  createUserEntity(userRequestDTO);
        return modelMapper.map(created, UserResponseDTO.class);
    }

    @Override
    public LoginResponseDTO login() {
        User user = updateJwt();
        updateUser(user);
        return modelMapper.map(user, LoginResponseDTO.class);
    }

    public UserEntity createUserEntity(UserRequestDTO userRequestDTO) {
        UserEntity userEntity = modelMapper.map(userRequestDTO, UserEntity.class);
        userEntity.setActive(true);
        userEntity.setCreated(LocalDateTime.now());
        userEntity.setLastLogin(LocalDateTime.now());
        userEntity.setToken(jwtUtil.createJWT(userRequestDTO.getEmail(), userEntity.getName(), userEntity.getEmail()));
        userEntity.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        UserEntity saved = userRepository.save(userEntity);
        Optional.ofNullable(userEntity.getPhones())
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .forEach(p -> p.setUser(saved));
        return saved;
    }

    public User updateUser(User user){
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(() -> new NotFoundException("Usuario inexistente"));
        userEntity.setLastLogin(LocalDateTime.now());
        userEntity.setToken(user.getToken());
        userRepository.save(userEntity);
        return modelMapper.map(userEntity, User.class);
    }

    public User updateJwt() {
        User user = requestUser.getUser();
        if(user == null) {
            throw new NotFoundException("Usuario inexistente");
        }
        user.setToken(jwtUtil.createJWT(user.getUsername(), user.getName(), user.getEmail()));
        return user;
    }

}
