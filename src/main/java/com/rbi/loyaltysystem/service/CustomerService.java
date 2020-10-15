package com.rbi.loyaltysystem.service;

import com.rbi.loyaltysystem.model.Customer;
import com.rbi.loyaltysystem.model.Point;
import com.rbi.loyaltysystem.model.Transaction;
import com.rbi.loyaltysystem.repository.api.InMemory;
import com.rbi.loyaltysystem.repository.api.TransactionRepositoryInMemory;
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

    private InMemory<Customer> customerRepository;
    private TransactionRepositoryInMemory transactionRepository;

    @Autowired
    public CustomerService(InMemory<Customer> customerRepository, TransactionRepositoryInMemory transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    public Customer getCustomer(long id) {
        return customerRepository.findById(id);
    }

    public long addCustomer(Customer customer) {
        return customerRepository.add(customer);
    }

    public Customer getDetails(long id){
        Customer customer = customerRepository.findById(id);
        updatePoints(id);
        return customer;
    }
    private boolean isValidSpendings(long id) {
        double spendings = transactionRepository.findSumOrderByDate(id);
        return spendings >= 500;
    }

    private boolean isValidLastTransaction(long id) {
        LocalDate lastTransactionDate = transactionRepository.findTransactionOrderByDate(id);
        LocalDate fiveWeekAgoDate = LocalDate.now().minusWeeks(5);
        return fiveWeekAgoDate.compareTo(lastTransactionDate) >= 0;
    }

    private boolean isValidWeeklyTransactions(long id) {
        List<Transaction> transactions = transactionRepository.findAllOrderByDate(id);
        Set<DayOfWeek> daysOfWeek = new HashSet<>();
        for (Transaction transaction : transactions){
            daysOfWeek.add(transaction.getDate().getDayOfWeek());
        }
        return daysOfWeek.size() == 7;
    }

    private void updatePoints(long id) {
        Customer customer = getCustomer(id);
        if (!isValidLastTransaction(id)){
            customer.setPoints(new ArrayList<>());
        }

        if (isValidWeeklyTransactions(id) && isValidSpendings(id)){
            List<Transaction> lastWeekTransactions = transactionRepository.findAllOrderByDate(id);
            List<Point> points = customer.getPoints();
            for(Transaction transaction : lastWeekTransactions){
                for (Point point : points){
                    if (transaction.getId() == point.getTransactionId()){
                        point.activate();
                    }
                }
            }
        }
    }
}
