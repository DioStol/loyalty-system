package com.rbi.loyaltysystem.repository.api;


import com.rbi.loyaltysystem.model.Investment;

import java.util.List;

public interface InvestmentRepository extends Repository<Investment> {

    List<Investment> findAllById(long id);
}
