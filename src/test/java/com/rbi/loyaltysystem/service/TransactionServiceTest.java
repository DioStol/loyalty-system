package com.rbi.loyaltysystem.service;

import com.rbi.loyaltysystem.exception.CustomerNotFoundException;
import com.rbi.loyaltysystem.exception.TransactionalException;
import com.rbi.loyaltysystem.model.Customer;
import com.rbi.loyaltysystem.model.Status;
import com.rbi.loyaltysystem.model.Transaction;
import com.rbi.loyaltysystem.repository.api.CustomerRepository;
import com.rbi.loyaltysystem.repository.api.InvestmentRepository;
import com.rbi.loyaltysystem.repository.api.TransactionRepository;
import org.junit.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TransactionServiceTest {

    private final CustomerRepository customerRepository = mock(CustomerRepository.class);

    private final TransactionRepository transactionRepository = mock(TransactionRepository.class);

    private final InvestmentRepository investmentRepository = mock(InvestmentRepository.class);

    private final TransactionService transactionService = new TransactionService(transactionRepository, customerRepository);

    private final CustomerService customerService = new CustomerService(customerRepository, transactionRepository, investmentRepository);

    @Test
    public void transactionTest() {
        mockCustomers();
        // Given
        long id = 0;
        Transaction transaction = new Transaction();
        transaction.setAmount(500);
        transaction.setSenderId(0);
        transaction.setRecipientId(1);
        // When
        transactionService.transact(transaction);
        Customer customer = customerService.getCustomer(id);
        Status status = customer.getPoints().get(0).getStatus();
        // Then
        assertEquals(1, customer.getPoints().size());
        assertEquals(Status.PENDING, status);
    }

    @Test
    public void transactionalExceptionTest() {
        mockCustomers();
        // Given
        Transaction firstTransaction = new Transaction();
        firstTransaction.setAmount(500);
        firstTransaction.setSenderId(0);
        firstTransaction.setRecipientId(0);

        Transaction secondTransaction = new Transaction();
        secondTransaction.setAmount(20000);
        secondTransaction.setSenderId(0);
        secondTransaction.setRecipientId(1);

        Transaction thirdTransaction = new Transaction();
        thirdTransaction.setAmount(-10);
        thirdTransaction.setRecipientId(0);
        thirdTransaction.setSenderId(1);
        // When
        TransactionalException firstException = assertThrows(firstTransaction);
        TransactionalException secondException = assertThrows(secondTransaction);
        TransactionalException thirdException = assertThrows(thirdTransaction);
        // Then
        assertNotNull(firstException);
        assertNotNull(secondException);
        assertNotNull(thirdException);
    }

    private TransactionalException assertThrows(Transaction transaction) {
        try {
            transactionService.transact(transaction);
        } catch (TransactionalException e) {
            return e;
        }
        return null;
    }

    private CustomerNotFoundException assertThrow(Transaction transaction) {
        try {
            transactionService.transact(transaction);
        } catch (CustomerNotFoundException e) {
            return e;
        }
        return null;
    }

    private void mockCustomers() {

        Customer firstCustomer = new Customer();
        firstCustomer.setBalance(10000);
        firstCustomer.setId(0);
        firstCustomer.setName("Jane Doe");

        Customer secondCustomer = new Customer();
        secondCustomer.setBalance(10000);
        secondCustomer.setId(1);
        secondCustomer.setName("John Doe");

        when(customerRepository.findById(0)).thenReturn(firstCustomer);
        when(customerRepository.findById(1)).thenReturn(secondCustomer);
    }
}