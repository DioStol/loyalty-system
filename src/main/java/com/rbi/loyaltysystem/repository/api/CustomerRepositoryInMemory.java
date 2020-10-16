package com.rbi.loyaltysystem.repository.api;

import com.rbi.loyaltysystem.dto.InvestmentDto;
import com.rbi.loyaltysystem.model.Customer;
import com.rbi.loyaltysystem.model.Point;

import java.util.List;

public interface CustomerRepositoryInMemory {

    List<Point> findAllPendingById(long id);
    List<Point> findAllAvailableById(long id);
    List<InvestmentDto> findAllInvestmentsById(long id);
    InvestmentDto invest(InvestmentDto investment);
    void update(Customer customer);
    Customer addIncome(long id, double income);
}
