package com.rbi.loyaltysystem.service;

import com.rbi.loyaltysystem.dto.TransactionDto;
import com.rbi.loyaltysystem.exception.TransactionalException;
import com.rbi.loyaltysystem.model.Customer;
import com.rbi.loyaltysystem.model.Point;
import com.rbi.loyaltysystem.model.Transaction;
import com.rbi.loyaltysystem.repository.api.CustomerRepository;
import com.rbi.loyaltysystem.repository.api.TransactionRepository;
import com.rbi.loyaltysystem.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }

    public Transaction transact(Transaction transaction) {
        execute(transaction);
        return transaction;
    }

    private void execute(final Transaction transaction) {
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

        synchronized (transaction) {

            transaction.setDate(LocalDate.now());
            transactionRepository.insert(transaction);

            Point point = new Point(transaction.getAmount(), transaction.getId());
            sender.withdraw(transaction.getAmount());
            recipient.deposit(transaction.getAmount());
            sender.addPoint(point);

            customerRepository.update(sender);
        }
    }

    public TransactionDto getTransactions(long id) {
        List<Transaction> transactions = transactionRepository.findAllOrderByCustomer(id);
        return Utils.convertTransactionsToDto(transactions);
    }
}
