package com.rbi.loyaltysystem.service;

import com.rbi.loyaltysystem.dto.InvestmentDto;
import com.rbi.loyaltysystem.dto.PointDto;
import com.rbi.loyaltysystem.model.Customer;
import com.rbi.loyaltysystem.model.Investment;
import com.rbi.loyaltysystem.model.Point;
import com.rbi.loyaltysystem.model.Transaction;
import com.rbi.loyaltysystem.repository.api.CustomerRepository;
import com.rbi.loyaltysystem.repository.api.InvestmentRepository;
import com.rbi.loyaltysystem.repository.api.TransactionRepository;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerServiceTest {

    private final CustomerRepository customerRepository = mock(CustomerRepository.class);

    private final TransactionRepository transactionRepository = mock(TransactionRepository.class);

    private final InvestmentRepository investmentRepository = mock(InvestmentRepository.class);

    private final CustomerService customerService = new CustomerService(customerRepository, transactionRepository, investmentRepository);

    @Test
    public void findAllInvestmentsTest() {
        mockInvestmentRepository();
        // Given
        long id = 1;
        // When
        InvestmentDto investmentDto = customerService.findAllInvestments(id);
        // Then
        assertEquals(2, investmentDto.getInvestments().size());
        assertEquals(300, investmentDto.getTotal(), 1E-15);
    }

    @Test
    public void findAllAvailableTest() {
        mockCustomerRepository();
        mockTransactionRepository();
        // Given
        long id = 0;
        // When
        PointDto pointDto = customerService.getAllAvailablePoints(id);
        // Then
        assertEquals(7, pointDto.getPoints().size());
        assertEquals(35, pointDto.getTotal(), 1E-15);
    }

    @Test
    public void investTest(){
        mockCustomerRepository();
        mockTransactionRepository();
        mockInvestmentRepository();
        // Given
        long id = 0;
        Investment investment = new Investment();
        investment.setDescription("Test");
        investment.setBalance(20);
        // When
        customerService.invest(investment);
        PointDto availablePointsDto = customerService.getAllAvailablePoints(id);
        // Then
        assertEquals(15, availablePointsDto.getTotal(), 1E-15);
    }

    @Test
    public void transactionTest(){
        mockCustomerRepository();
        mockTransactionRepository();
        // Given
        long id = 0;
        Transaction transaction = new Transaction();
        transaction.setAmount(500);
        transaction.setSenderId(0);
        transaction.setRecipientId(1);
        // When

    }

    private void mockInvestmentRepository() {
        List<Investment> investments = new ArrayList<>();
        Investment investment = new Investment();
        investment.setBalance(100);
        investment.setCustomerId(1);
        investment.setDescription("Test");
        investment.setId(1);
        investment.setDate(LocalDate.now());
        investments.add(investment);
        investment = new Investment();
        investment.setBalance(200);
        investment.setCustomerId(1);
        investment.setDescription("Test");
        investment.setId(2);
        investment.setDate(LocalDate.now());
        investments.add(investment);
        when(investmentRepository.findAllById(1)).thenReturn(investments);
    }

    private void mockCustomerRepository() {

        Customer firstCustomer = new Customer();
        firstCustomer.setBalance(10000);
        firstCustomer.setId(0);
        firstCustomer.setName("Jane Doe");
        firstCustomer.setPoints(getPendingPoints());

        Customer secondCustomer = new Customer();
        secondCustomer.setBalance(10000);
        secondCustomer.setId(1);
        secondCustomer.setName("John Doe");

        when(customerRepository.findAllPendingById(0)).thenReturn(getPendingPoints());
        when(customerRepository.findAllAvailableById(0)).thenReturn(getAvailablePoints());
        when(customerService.getCustomer(0)).thenReturn(firstCustomer);

        when(customerService.getCustomer(1)).thenReturn(secondCustomer);
    }

    private void mockTransactionRepository() {
        Transaction transaction = new Transaction();
        transaction.setId(0);
        transaction.setRecipientId(1);
        transaction.setAmount(500);
        List<Transaction> rightWeeklyTransactions = getRightWeeklyTransactions();
        List<Transaction> wrongWeeklyTransactions = getWrongWeeklyTransactions();
        LocalDate rightLastTransactionDate = rightWeeklyTransactions.get(rightWeeklyTransactions.size() - 1).getDate();
        LocalDate wrongLastTransactionDate = wrongWeeklyTransactions.get(wrongWeeklyTransactions.size() - 1).getDate();

        when(transactionRepository.findAllOrderByDate(0)).thenReturn(rightWeeklyTransactions);
        when(transactionRepository.findLastTransactionDateById(0)).thenReturn(rightLastTransactionDate);
        when(transactionRepository.findSumOrderByDate(0)).thenReturn(getLastWeekSpendings(rightWeeklyTransactions));

        when(transactionRepository.findAllOrderByDate(1)).thenReturn(wrongWeeklyTransactions);
        when(transactionRepository.findLastTransactionDateById(1)).thenReturn(wrongLastTransactionDate);
        when(transactionRepository.findSumOrderByDate(1)).thenReturn(getLastWeekSpendings(rightWeeklyTransactions));
    }

    private List<Point> getPendingPoints() {
        List<Point> pendingPoints = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Point point = new Point(500,0);
            pendingPoints.add(point);
        }
        return pendingPoints;
    }

    private List<Point> getAvailablePoints() {
        List<Point> availablePoints = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Point point = new Point(500,1);
            point.activate();
            availablePoints.add(point);
        }
        return availablePoints;
    }

    private List<Transaction> getRightWeeklyTransactions() {
        List<Transaction> rightWeeklyTransactions = new ArrayList<>();
        LocalDate date = LocalDate.now().minusWeeks(1);
        for (int i = 0; i < 7; i++) {
            Transaction transaction = new Transaction();
            transaction.setId(0);
            transaction.setSenderId(0);
            transaction.setRecipientId(1);
            transaction.setAmount(500);
            transaction.setDate(date.plusDays(i));
            rightWeeklyTransactions.add(transaction);
        }
        return rightWeeklyTransactions;
    }

    private double getLastWeekSpendings(List<Transaction> transactions) {
        double spendings = 0;
        LocalDate lastWeek = LocalDate.now().minusWeeks(1);
        for (Transaction transaction : transactions) {
            if (transaction.getDate().compareTo(lastWeek) >= 0) {
                spendings += transaction.getAmount();
            }
        }
        return spendings;
    }

    private List<Transaction> getWrongWeeklyTransactions() {
        Transaction transaction = new Transaction();
        List<Transaction> transactions = new ArrayList<>();
        transaction.setId(1);
        transaction.setRecipientId(0);
        transaction.setSenderId(1);
        transaction.setAmount(500);
        transactions.add(transaction);
        return transactions;
    }
}