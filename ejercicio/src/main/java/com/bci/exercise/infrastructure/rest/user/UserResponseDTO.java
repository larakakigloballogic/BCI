package com.bci.exercise.infrastructure.rest.user;

import com.bci.exercise.infrastructure.rest.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class UserResponseDTO extends ResponseDTO {

    /*
    todo: formato     created": "Nov 16, 2021 12:51:43 PM"
     */
    private UUID id;
    private LocalDateTime created;
    private LocalDateTime lastLogin;
    private String token;
    private boolean isActive;

}
