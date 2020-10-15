package com.rbi.loyaltysystem.service;

import com.rbi.loyaltysystem.exception.TransactionalException;
import com.rbi.loyaltysystem.model.Customer;
import com.rbi.loyaltysystem.model.Point;
import com.rbi.loyaltysystem.model.Transaction;
import com.rbi.loyaltysystem.repository.CustomerRepository;
import com.rbi.loyaltysystem.repository.TransactionRepository;
import com.rbi.loyaltysystem.repository.api.InMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    private CustomerRepository customerRepository;

    private final Object lockTransaction = new Object();

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
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
                throw new TransactionalException();
            }

            if (sender.getId() == recipient.getId()){
                throw new TransactionalException();
            }

            if (transaction.getAmount() < 0){
                throw new TransactionalException();
            }

            transaction.setDate(LocalDate.now());
            transactionRepository.add(transaction);

            int points = calculatePoints(transaction.getAmount());
            Point point = new Point(points, transaction.getId());
            sender.withdraw(transaction.getAmount());
            recipient.deposit(transaction.getAmount());
            sender.addPoint(point);

            customerRepository.update(sender);
        }
    }

    private int calculatePoints(double amount) {
        int points;
        if (amount <= 5000) {
            points = (int) amount;
        } else if (amount <= 7500) {
            points = (int) (5000 + (amount - 5001) * 2);
        } else {
            points = (int) (5000 + (amount - 5000) * 2 + (amount - 75001) * 3);
        }
        return points;
    }
}
