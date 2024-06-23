package com.bci.exercise.service;

import com.bci.exercise.dto.LoginResponseDTO;
import com.bci.exercise.dto.UserRequestDTO;
import com.bci.exercise.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    LoginResponseDTO login();

}
