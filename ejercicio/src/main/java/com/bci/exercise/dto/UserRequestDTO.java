package com.bci.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @Pattern(regexp = "^(?=.*[A-Z])(?!.*[A-Z].*[A-Z])(?=(?:[^0-9]*[0-9]){2}[^0-9]*$)(?!.*\\s).*$", message = "Debe contener exactamente una mayúscula y dos números")
    private String password;

    private Set<PhoneRequestDTO> phones;


}
