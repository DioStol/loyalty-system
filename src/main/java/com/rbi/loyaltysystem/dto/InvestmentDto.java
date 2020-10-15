package com.rbi.loyaltysystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvestmentDto {

    private long id;

    private String description;

    private double balance;

    private long customerId;
}
