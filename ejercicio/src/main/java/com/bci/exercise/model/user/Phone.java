package com.bci.exercise.model.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Phone {

    private long id;
    private long number;
    private int citycode;
    private String contrycode;

}
