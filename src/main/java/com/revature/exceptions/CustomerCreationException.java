package com.revature.exceptions;

public class CustomerCreationException extends RuntimeException {
    public CustomerCreationException(String msg) {
        super(msg);
    }
}
