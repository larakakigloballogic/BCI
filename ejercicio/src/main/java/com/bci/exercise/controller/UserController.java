package com.bci.exercise.controller;

import com.bci.exercise.dto.LoginResponseDTO;
import com.bci.exercise.dto.UserRequestDTO;
import com.bci.exercise.dto.UserResponseDTO;
import com.bci.exercise.service.UserService;
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
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(path = "/sign-up",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> singUp(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        final UserResponseDTO responseDTO = this.userService.createUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping(path = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponseDTO> login() {
        final LoginResponseDTO loginResponseDTO = this.userService.login();
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDTO);
    }

}
