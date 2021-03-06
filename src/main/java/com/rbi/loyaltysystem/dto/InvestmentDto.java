package com.rbi.loyaltysystem.dto;

import com.rbi.loyaltysystem.model.Investment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class InvestmentDto {

    private List<Investment> investments;

    private double total;
}
