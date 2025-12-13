package com.example.campusjobboard.exception;

public class JobNotFoundException extends RuntimeException {

    public JobNotFoundException(String message) {
        super(message);
    }
}

