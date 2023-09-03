package com.bci.exercise.infrastructure.rest.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 8, max = 12)
    //@Pattern(regexp = "")
    private String password;

    private Set<PhoneRequestDTO> phones;


}
