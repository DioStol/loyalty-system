package com.rbi.loyaltysystem.repository;

import com.rbi.loyaltysystem.dto.InvestmentDto;
import com.rbi.loyaltysystem.exception.CustomerNotFoundException;
import com.rbi.loyaltysystem.exception.TransactionalException;
import com.rbi.loyaltysystem.model.Customer;
import com.rbi.loyaltysystem.model.Point;
import com.rbi.loyaltysystem.model.Status;
import com.rbi.loyaltysystem.model.Transaction;
import com.rbi.loyaltysystem.repository.api.CustomerRepositoryInMemory;
import com.rbi.loyaltysystem.repository.api.InMemory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Repository
public class CustomerRepository implements InMemory<Customer>, CustomerRepositoryInMemory {

    private List<Customer> customers;

    private List<InvestmentDto> investments;

    public CustomerRepository() {
        this.customers = new ArrayList<>();
        this.investments = new ArrayList<>();
    }

    @Override
    public Customer findById(long id) {
        if (customers.size() == 0) {
            throw new CustomerNotFoundException();
        }
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                return customer;
            }
        }
        throw new CustomerNotFoundException();
    }

    @Override
    public void update(Customer customer) {
        Customer c = customers.get((int) customer.getId());
        c.setBalance(customer.getBalance());
        c.setPoints(customer.getPoints());
    }

    @Override
    public Customer addIncome(long id, double income) {
        Customer customer = findById(id);
        if (income < 0){
            throw new TransactionalException();
        }
        double balance = customer.getBalance();
        balance += income;
        customer.setBalance(balance);
        return customer;
    }

    @Override
    public long add(Customer customer) {
        customer.setId(customers.size());
        customers.add(customer);
        return customer.getId();
    }

    @Override
    public List<Point> findAllPendingById(long id) {
        Customer customer = findById(id);
        List<Point> points = customer.getPoints();
        List<Point> pendingPoints = new ArrayList<>();
        for(Point point : points){
            if (point.getStatus() == Status.PENDING){
                pendingPoints.add(point);
            }
        }
        return pendingPoints;
    }

    @Override
    public List<Point> findAllAvailableById(long id) {
        Customer customer = findById(id);
        List<Point> points = customer.getPoints();
        List<Point> availablePoints = new ArrayList<>();
        for(Point point : points){
            if (point.getStatus() == Status.AVAILABLE){
                availablePoints.add(point);
            }
        }
        return availablePoints;
    }

    @Override
    public List<InvestmentDto> findAllInvestmentsById(long id) {
        List<InvestmentDto> customerInvestments = new ArrayList<>();
        for(InvestmentDto invest : investments){
            if (invest.getCustomerId() == id){
                customerInvestments.add(invest);
            }
        }
        return customerInvestments;
    }

    @Override
    public InvestmentDto invest(InvestmentDto investment) {
        Customer customer = findById(investment.getCustomerId());
//        List<Point> points = findAllAvailableById(investment.getCustomerId());
//        if (points.size() == 0){
//            throw new TransactionalException();
//        }
        List<Point> points = customer.getPoints();
        double pointsBalance = getPointsBalance(points);
        if (investment.getBalance() > pointsBalance){
            throw new TransactionalException();
        }
        double earnings = 0;
        for (Point point : points){
            earnings += point.getEarnings();
            point.deactivate();
            updateCustomerPoint(customer, point);
            if (earnings == investment.getBalance()){
                break;
            }
        }
        //External transaction implementation
        investment.setId(investments.size());
        investment.setDate(LocalDate.now());
        investments.add(investment);
        return investment;
    }

    private double getPointsBalance(List<Point> points){
        double balance = 0;
        for (Point point : points){
            balance += point.getEarnings();
        }
        return balance;
    }

    private void updateCustomerPoint(Customer customer, Point point){
        List<Point> copiedPoints = customer.getPoints();
        int index = 0;
        for (int i = 0; i<copiedPoints.size(); i++){
            if (customer.getPoints().get(i).getTransactionId() == point.getTransactionId()){
                index = i;
            }
        }
        copiedPoints.get(index).setEarnings(point.getEarnings());
        copiedPoints.get(index).setStatus(point.getStatus());
    }

    public void activateLastWeekPoints(Customer customer, List<Transaction> lastWeekTransactions){
        List<Point> points = customer.getPoints();
        for(Transaction transaction : lastWeekTransactions){
            for (Point point : points){
                if (transaction.getId() == point.getTransactionId()){
                    point.activate();
                    updateCustomerPoint(customer, point);
                }
            }
        }
    }
}
