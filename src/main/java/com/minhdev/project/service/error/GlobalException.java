package com.minhdev.project.service.error;

import com.minhdev.project.domain.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<RestResponse<Object>> idInvalidException(IdInvalidException exception) {
        RestResponse<Object> restResponse = new RestResponse<Object>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setMessage(exception.getMessage());
        restResponse.setError("IdInvalidException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }
}
