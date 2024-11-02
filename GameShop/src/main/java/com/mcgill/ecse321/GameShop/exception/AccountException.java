package com.mcgill.ecse321.GameShop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

public class AccountException extends RuntimeException {
    @NonNull
    private HttpStatus status;

    public AccountException(@NonNull HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    @NonNull
    public HttpStatus getStatus() {
        return status;
    }
}
