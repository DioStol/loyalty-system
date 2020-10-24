package com.rbi.loyaltysystem.repository;

import com.rbi.loyaltysystem.exception.CustomerNotFoundException;
import com.rbi.loyaltysystem.exception.TransactionalException;
import com.rbi.loyaltysystem.model.*;
import com.rbi.loyaltysystem.repository.api.CustomerRepository;
import com.rbi.loyaltysystem.util.Utils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class CustomerInMemoryRepository implements CustomerRepository {

    private final List<Customer> customers;

    public CustomerInMemoryRepository() {
        this.customers = new ArrayList<>();
    }

    @Override
    public Customer findById(long id) {
        if (customers.isEmpty()) {
            throw new CustomerNotFoundException(Utils.NOT_AVAILABLE_CUSTOMERS);
        }
        if (customers.size() <= id || id < 0) {
            throw new CustomerNotFoundException(Utils.CUSTOMER_DOES_NOT_EXISTS);
        }
        return customers.get((int) id);
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
        if (income < 0) {
            throw new TransactionalException(Utils.NEGATIVE_AMOUNT);
        }
        double balance = customer.getBalance();
        balance += income;
        customer.setBalance(balance);
        return customer;
    }

    @Override
    public Customer insert(Customer customer) {
        customer.setId(customers.size());
        customers.add(customer);
        return customer;
    }


    @Override
    public List<Point> findAllPendingById(long id) {
        Customer customer = findById(id);
        List<Point> points = customer.getPoints();
        List<Point> pendingPoints = new ArrayList<>();
        for (Point point : points) {
            if (point.getStatus() == Status.PENDING) {
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
        for (Point point : points) {
            if (point.getStatus() == Status.AVAILABLE) {
                availablePoints.add(point);
            }
        }
        return availablePoints;
    }
}
