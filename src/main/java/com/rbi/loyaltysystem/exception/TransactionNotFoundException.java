package com.rbi.loyaltysystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseBody
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(String msg) {
        super(msg);
    }
}