package com.rbi.loyaltysystem.repository;

import com.rbi.loyaltysystem.model.Investment;
import com.rbi.loyaltysystem.repository.api.InvestmentRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InvestmentInMemoryRepository implements InvestmentRepository {

    private final List<Investment> investments;

    public InvestmentInMemoryRepository() {
        this.investments = new ArrayList<>();
    }

    @Override
    public List<Investment> findAllById(long id) {
        List<Investment> customerInvestments = new ArrayList<>();
        for (Investment invest : investments) {
            if (invest.getCustomerId() == id) {
                customerInvestments.add(invest);
            }
        }
        return customerInvestments;
    }

    @Override
    public Investment insert(Investment investment) {
        investment.setId(investments.size());
        investment.setDate(LocalDate.now());
        investments.add(investment);
        return investment;
    }

    @Override
    public Investment findById(long id) {
        Investment investment = findById(id);
        investment.setId(investments.size());
        investment.setDate(LocalDate.now());
        investments.add(investment);
        return investment;
    }
}
