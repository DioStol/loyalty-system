package com.rbi.loyaltysystem.service;

import com.rbi.loyaltysystem.dto.InvestmentDto;
import com.rbi.loyaltysystem.dto.PointDto;
import com.rbi.loyaltysystem.model.Customer;
import com.rbi.loyaltysystem.model.Investment;
import com.rbi.loyaltysystem.model.Point;
import com.rbi.loyaltysystem.model.Transaction;
import com.rbi.loyaltysystem.repository.CustomerRepository;
import com.rbi.loyaltysystem.repository.api.TransactionRepositoryInMemory;
import com.rbi.loyaltysystem.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;
    private TransactionRepositoryInMemory transactionRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, TransactionRepositoryInMemory transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    public Customer getCustomer(long id) {
        return customerRepository.findById(id);
    }

    public long addCustomer(Customer customer) {
        return customerRepository.add(customer);
    }

    public Investment invest(Investment investment) {
        updatePoints(investment.getCustomerId());
        return customerRepository.invest(investment);
    }

    public Customer addIncome(long id, double income) {
        return customerRepository.addIncome(id, income);
    }

    public InvestmentDto findAllInvestments(long id) {
        List<Investment> investments = customerRepository.findAllInvestmentsById(id);
        return Utils.convertInvestmentsToDto(investments);
    }

    public PointDto getAllPendingPoints(long id) {
        updatePoints(id);
        List<Point> pendingPoints = customerRepository.findAllPendingById(id);
        return Utils.convertPointsToDto(pendingPoints);
    }

    public PointDto getAllAvailablePoints(long id) {
        updatePoints(id);
        List<Point> availablePoints = customerRepository.findAllAvailableById(id);
        return Utils.convertPointsToDto(availablePoints);
    }

    private boolean isValidSpendings(long id) {
        double spendings = transactionRepository.findSumOrderByDate(id);
        return spendings >= 500;
    }

    private boolean isValidLastTransaction(long id) {
        LocalDate lastTransactionDate = transactionRepository.findTransactionOrderByDate(id);
        LocalDate fiveWeekAgoDate = LocalDate.now().minusWeeks(5);
        return lastTransactionDate.compareTo(fiveWeekAgoDate) >= 0;
    }

    private boolean isValidWeeklyTransactions(long id) {
        List<Transaction> lastWeekTransactions = transactionRepository.findAllOrderByDate(id);
        Set<DayOfWeek> daysOfWeek = new HashSet<>();
        for (Transaction transaction : lastWeekTransactions) {
            daysOfWeek.add(transaction.getDate().getDayOfWeek());
        }
        return daysOfWeek.size() == 7;
    }

    private void updatePoints(long id) {
        Customer customer = getCustomer(id);
        if (!isValidLastTransaction(id)) {
            customer.setPoints(new ArrayList<>());
        }

        if (isValidWeeklyTransactions(id) && isValidSpendings(id)) {
            List<Transaction> lastWeekTransactions = transactionRepository.findAllOrderByDate(id);
            customerRepository.activateLastWeekPoints(customer, lastWeekTransactions);
        }
    }
}
