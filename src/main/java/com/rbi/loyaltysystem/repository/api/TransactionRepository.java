package com.rbi.loyaltysystem.repository.api;

import com.rbi.loyaltysystem.model.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends Repository<Transaction> {

    LocalDate findTransactionOrderByDate(long id);
    double findSumOrderByDate(long id);
    List<Transaction> findAllOrderByCustomer(long id);
    List<Transaction> findAllOrderByDate(long id);
}
