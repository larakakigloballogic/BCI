package com.bci.exercise.domain.user;

import lombok.Data;

@Data
public class Phone {

    private long id;
    private long number;
    private int citycode;
    private String contrycode;

}
