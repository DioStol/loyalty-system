package com.rbi.loyaltysystem.repository.api;

import com.rbi.loyaltysystem.model.Customer;
import com.rbi.loyaltysystem.model.Investment;
import com.rbi.loyaltysystem.model.Point;

import java.util.List;

public interface CustomerRepository extends Repository<Customer> {

    List<Point> findAllPendingById(long id);
    List<Point> findAllAvailableById(long id);
    void update(Customer customer);
    Customer addIncome(long id, double income);
}
