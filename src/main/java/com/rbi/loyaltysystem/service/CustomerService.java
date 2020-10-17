package com.rbi.loyaltysystem.service;

import com.rbi.loyaltysystem.dto.InvestmentDto;
import com.rbi.loyaltysystem.dto.PointDto;
import com.rbi.loyaltysystem.exception.TransactionalException;
import com.rbi.loyaltysystem.model.Customer;
import com.rbi.loyaltysystem.model.Investment;
import com.rbi.loyaltysystem.model.Point;
import com.rbi.loyaltysystem.model.Transaction;
import com.rbi.loyaltysystem.repository.api.CustomerRepository;
import com.rbi.loyaltysystem.repository.api.InvestmentRepository;
import com.rbi.loyaltysystem.repository.api.TransactionRepository;
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

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final InvestmentRepository investmentRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, TransactionRepository transactionRepository, InvestmentRepository investmentRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
        this.investmentRepository = investmentRepository;
    }

    public Customer getCustomer(long id) {
        return customerRepository.findById(id);
    }

    public Customer addCustomer(Customer customer) {
        return customerRepository.insert(customer);
    }

    public Customer addIncome(long id, double income) {
        return customerRepository.addIncome(id, income);
    }

    public InvestmentDto findAllInvestments(long id) {
        List<Investment> investments = investmentRepository.findAllById(id);
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
        LocalDate lastTransactionDate = transactionRepository.findLastTransactionDateById(id);
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
            activateLastWeekPoints(customer, lastWeekTransactions);
        }
    }

    public void activateLastWeekPoints(Customer customer, List<Transaction> lastWeekTransactions) {
        List<Point> points = customerRepository.findAllPendingById(customer.getId());
        for (Transaction transaction : lastWeekTransactions) {
            for (Point point : points) {
                if (transaction.getId() == point.getTransactionId()) {
                    point.activate();
                    updateCustomerPoint(customer, point);
                }
            }
        }
    }

    public Investment invest(Investment investment) {
        Customer customer = customerRepository.findById(investment.getCustomerId());
        List<Point> points = customerRepository.findAllAvailableById(investment.getCustomerId());
        if (points.isEmpty()) {
            throw new TransactionalException();
        }

        double pointsBalance = getPointsBalance(points);
        if (investment.getBalance() > pointsBalance) {
            throw new TransactionalException();
        }
        double earnings = 0;
        for (Point point : points) {
            earnings += point.getEarnings();
            point.deactivate();
            if (earnings >= investment.getBalance()) {
                if (earnings > investment.getBalance()) {
                    double difference = earnings - investment.getBalance();
                    point.setEarnings(difference);
                    point.activate();
                }
                break;
            }
            // External investment transaction
            updateCustomerPoint(customer, point);
        }
        return investmentRepository.insert(investment);
    }

    private double getPointsBalance(List<Point> points) {
        double balance = 0;
        for (Point point : points) {
            balance += point.getEarnings();
        }
        return balance;
    }

    private void updateCustomerPoint(Customer customer, Point point) {
        List<Point> copiedPoints = customer.getPoints();
        int index = 0;
        for (int i = 0; i < copiedPoints.size(); i++) {
            if (customer.getPoints().get(i).getTransactionId() == point.getTransactionId()) {
                index = i;
            }
        }
        copiedPoints.get(index).setEarnings(point.getEarnings());
        copiedPoints.get(index).setStatus(point.getStatus());
    }
}
