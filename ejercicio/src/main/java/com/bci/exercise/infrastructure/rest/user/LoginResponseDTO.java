package com.bci.exercise.infrastructure.rest.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class LoginResponseDTO extends UserResponseDTO {

    private String name;
    private String email;
    private String password;
    private Set<PhoneDTO> phones;

}
