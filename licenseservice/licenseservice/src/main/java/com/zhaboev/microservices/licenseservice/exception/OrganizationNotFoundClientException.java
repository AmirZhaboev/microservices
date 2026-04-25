package com.zhaboev.microservices.licenseservice.exception;

public class OrganizationNotFoundClientException extends RuntimeException {
    public OrganizationNotFoundClientException(String message) {
        super(message);
    }
}
