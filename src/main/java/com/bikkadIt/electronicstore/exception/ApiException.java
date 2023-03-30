package com.bikkadIt.electronicstore.exception;

public class ApiException extends RuntimeException{
    public ApiException(String message)
    {
        super(message);
    }
    public ApiException() {
        super();

    }
}
