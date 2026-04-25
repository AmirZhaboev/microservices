package com.zhaboev.microservices.organizationservice.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(OrganizationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleOrganizationNotFound(OrganizationNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Organization not found");
        response.put("message", ex.getMessage());

        return response;
    }
}
