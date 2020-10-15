package com.rbi.loyaltysystem.controller;


import com.rbi.loyaltysystem.exception.CustomerNotFoundException;
import com.rbi.loyaltysystem.exception.TransactionNotFoundException;
import com.rbi.loyaltysystem.exception.TransactionalException;
import com.rbi.loyaltysystem.model.Transaction;
import com.rbi.loyaltysystem.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/transactions")
public class TransactionController {

    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> makeTransaction(@RequestBody final Transaction transaction) {
        return ResponseEntity.ok(transactionService.transact(transaction));
    }
}
