package com.rbi.loyaltysystem.controller;


import com.rbi.loyaltysystem.dto.TransactionDto;
import com.rbi.loyaltysystem.exception.CustomerNotFoundException;
import com.rbi.loyaltysystem.exception.ExceptionResponse;
import com.rbi.loyaltysystem.exception.TransactionNotFoundException;
import com.rbi.loyaltysystem.exception.TransactionalException;
import com.rbi.loyaltysystem.model.Transaction;
import com.rbi.loyaltysystem.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> makeTransaction(@RequestBody final Transaction transaction) {
        return ResponseEntity.ok(transactionService.transact(transaction));
    }

    @GetMapping
    public ResponseEntity<TransactionDto> getTransactions(@RequestParam Long id) {
        return ResponseEntity.ok(transactionService.getTransactions(id));
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<?> notFoundException(HttpServletRequest request, TransactionNotFoundException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionalException.class)
    public ResponseEntity<?> badTransactionException(HttpServletRequest request, TransactionalException e){
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
