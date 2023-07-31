package com.mycompany.advertising.web.controller;

import com.mycompany.advertising.api.exceptions.CreateTokenException;
import com.mycompany.advertising.api.exceptions.PhoneNumberFormatException;
import com.mycompany.advertising.api.exceptions.SendSmsException;
import com.mycompany.advertising.api.exceptions.UserAlreadyExistException;
import com.mycompany.advertising.api.locker.exceptions.CallRestApiLimitException;
import com.mycompany.advertising.api.locker.exceptions.CallWebApiLimitException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Amir on 8/4/2022.
 */
@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CallRestApiLimitException.class})
    public ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {CallWebApiLimitException.class})
    public ResponseEntity<Object> webApiLimitException(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {SendSmsException.class,PhoneNumberFormatException.class,CreateTokenException.class})
    public ResponseEntity<Object> createTokenException(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
    }
    @ExceptionHandler(value = {UserAlreadyExistException.class})
    public ResponseEntity<Object> UuerAlreadyExistException(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }


    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> result = new ArrayList<>();
        for (FieldError fe : fieldErrors) {
            result.add(fe.getDefaultMessage());
        }
        return new ResponseEntity<>(result, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(),error.getDefaultMessage());
        });
        //return new ResponseEntity<>(ex.getMessage(), status);
        return new ResponseEntity<>(errorMap, status);
    }
}
