package com.rbi.loyaltysystem.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponse {

    private String msg;

    private String uri;
}
