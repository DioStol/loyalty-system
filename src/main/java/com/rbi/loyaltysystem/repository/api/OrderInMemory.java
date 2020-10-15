package com.rbi.loyaltysystem.repository.api;

import com.rbi.loyaltysystem.model.Transaction;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Dionysios Stolis 10/15/2020 <dstolis@b-open.com>
 */
public interface OrderInMemory {

    LocalDate findTransactionOrderByDate(long id);
    double findSumOrderByDate(long id);
    List<Transaction> findAllOrderByCustomer(long id);
    List<Transaction> findAllOrderByDate(long id);
}
