package com.rbi.loyaltysystem.service;

import com.rbi.loyaltysystem.dto.TransactionDto;
import com.rbi.loyaltysystem.exception.TransactionalException;
import com.rbi.loyaltysystem.model.Customer;
import com.rbi.loyaltysystem.model.Point;
import com.rbi.loyaltysystem.model.Transaction;
import com.rbi.loyaltysystem.repository.TransactionInMemoryRepository;
import com.rbi.loyaltysystem.repository.api.CustomerRepository;
import com.rbi.loyaltysystem.repository.api.TransactionRepository;
import com.rbi.loyaltysystem.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionInMemoryRepository;
    private final CustomerRepository customerRepository;

    private final Object lockTransaction = new Object();

    @Autowired
    public TransactionService(TransactionRepository transactionInMemoryRepository, CustomerRepository customerRepository) {
        this.transactionInMemoryRepository = transactionInMemoryRepository;
        this.customerRepository = customerRepository;
    }

    public Transaction transact(Transaction transaction) {
        execute(transaction);
        return transaction;
    }

    private void execute(Transaction transaction) {
        synchronized (lockTransaction) {
            Customer sender = customerRepository.findById(transaction.getSenderId());
            Customer recipient = customerRepository.findById(transaction.getRecipientId());

            if (sender.getBalance() < transaction.getAmount()) {
                throw new TransactionalException(Utils.LOWER_BALANCE);
            }

            if (sender.getId() == recipient.getId()) {
                throw new TransactionalException(Utils.TRANSFER_TO_SAME_ACCOUNT);
            }

            if (transaction.getAmount() < 0) {
                throw new TransactionalException(Utils.NEGATIVE_AMOUNT);
            }

            transaction.setDate(LocalDate.now());
            transactionInMemoryRepository.insert(transaction);

            int points = calculatePoints(transaction.getAmount());
            Point point = new Point(points, transaction.getId());
            sender.withdraw(transaction.getAmount());
            recipient.deposit(transaction.getAmount());
            sender.addPoint(point);

            customerRepository.update(sender);
        }
    }

    public TransactionDto getTransactions(long id) {
        List<Transaction> transactions = transactionInMemoryRepository.findAllOrderByCustomer(id);
        return Utils.convertTransactionsToDto(transactions);
    }

    private int calculatePoints(double amount) {
        int points;
        if (amount <= 5000) {
            points = (int) amount;
        } else if (amount <= 7500) {
            int x = (int) amount - 5000;
            points = 5000;
            points += x * 2;
        } else {
            points = 10000;
            int x = (int) amount - 7500;
            points += x * 3;
        }
        return points;
    }
}
