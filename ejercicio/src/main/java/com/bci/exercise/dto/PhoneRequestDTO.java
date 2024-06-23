package com.bci.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneRequestDTO {

    private long number;
    private int citycode;
    private String contrycode;

}
