package com.bci.exercise.infrastructure.rest.user;

import com.bci.exercise.domain.user.User;
import com.bci.exercise.domain.user.UserRestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserRestService userRestService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(path = "/sign-up",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> singUp(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        User user = modelMapper.map(userRequestDTO, User.class);
        User created = userRestService.createUser(user);
        UserResponseDTO responseDTO = modelMapper.map(created, UserResponseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping(path = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponseDTO> login() {
        User user = userRestService.login();
        LoginResponseDTO loginResponseDTO = modelMapper.map(user, LoginResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDTO);
    }

}
