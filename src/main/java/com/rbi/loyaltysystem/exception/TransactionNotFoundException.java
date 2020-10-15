package com.rbi.loyaltysystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseBody
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Transaction does not exists")
public class TransactionNotFoundException extends RuntimeException {
}