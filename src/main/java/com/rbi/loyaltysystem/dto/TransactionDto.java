package com.rbi.loyaltysystem.dto;

import com.rbi.loyaltysystem.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TransactionDto {

    private List<Transaction> transactions;

    private double total;
}
