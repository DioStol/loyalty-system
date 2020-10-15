package com.rbi.loyaltysystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Dionysios Stolis 10/14/2020 <dstolis@b-open.com>
 */
@ResponseBody
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TransactionalException extends RuntimeException {

    public TransactionalException(String msg) {
        super(msg);
    }
}
