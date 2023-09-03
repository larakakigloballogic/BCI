package com.bci.exercise.infrastructure.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ErrorDTO> error;

    @Builder
    @Data
    public static class ErrorDTO {
        private Timestamp timestamp;
        private int codigo;
        private String detail;
    }
}
