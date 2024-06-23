package com.bci.exercise.config.exception;


import com.bci.exercise.dto.ResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;

@Log4j2
@ControllerAdvice
class ErrorHandler extends ResponseEntityExceptionHandler {

    private static final int DEFAULT_ERR = 10000;
    private static final int EXISTED_USER_ERR = 10001;

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResponseDTO> handle(Exception e, HttpServletRequest request) {
        log.error(e.toString());
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setError(Arrays.asList(getErrorDTO(e, DEFAULT_ERR)));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
    }

    private ResponseDTO.ErrorDTO getErrorDTO(Exception e, int codigo) {
        return ResponseDTO.ErrorDTO.builder()
                .codigo(codigo)
                .timestamp(Timestamp.from(Instant.now()))
                .detail(e.getMessage())
                .build();
    }

    @ExceptionHandler({CredentialsExpiredException.class, ExpiredJwtException.class})
    public ResponseEntity<ResponseDTO> handleCredentialsExpiredException(Exception e, HttpServletRequest request) {
        log.error(e.toString());
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setError(Arrays.asList(getErrorDTO(e, DEFAULT_ERR)));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDTO);
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<ResponseDTO> handleUsernameNotFoundException(Exception e, HttpServletRequest request) {
        log.error(e.toString());
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setError(Arrays.asList(getErrorDTO(e, DEFAULT_ERR)));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
    }

    @ExceptionHandler({UserException.class})
    public ResponseEntity<ResponseDTO> handleUserException(Exception e, HttpServletRequest request) {
        log.error(e.toString());
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setError(Arrays.asList(getErrorDTO(e, EXISTED_USER_ERR)));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseDTO);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(e.toString());
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setError(Arrays.asList(getErrorDTO(e, DEFAULT_ERR)));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
    }

    @Override
    public ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(e.toString());
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setError(Arrays.asList(getErrorDTO(e, DEFAULT_ERR)));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
    }

}
