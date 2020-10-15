package com.rbi.loyaltysystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseBody
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(long id) {
        super("Customer with id = " + id + " doesn't exist.");
    }
}
