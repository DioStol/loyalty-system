package com.rbi.loyaltysystem.repository;

import com.rbi.loyaltysystem.dto.InvestmentDto;
import com.rbi.loyaltysystem.exception.CustomerNotFoundException;
import com.rbi.loyaltysystem.model.Customer;
import com.rbi.loyaltysystem.model.Point;
import com.rbi.loyaltysystem.repository.api.CustomerRepositoryInMemory;
import com.rbi.loyaltysystem.repository.api.InMemory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class CustomerRepository implements InMemory<Customer>, CustomerRepositoryInMemory {

    private List<Customer> customers;

    public CustomerRepository() {
        this.customers = new ArrayList<>();
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
    public long add(Customer customer) {
        customer.setId(customers.size());
        customers.add(customer);
        return customer.getId();
    }

    @Override
    public List<Point> findAllPendingById(long id) {
        return null;
    }

    @Override
    public List<Point> findAllAvailableById(long id) {
        return null;
    }

    @Override
    public List<InvestmentDto> findAllInvestmentsById(long id) {
        return null;
    }

    @Override
    public void invest(InvestmentDto investment) {

    }
}
