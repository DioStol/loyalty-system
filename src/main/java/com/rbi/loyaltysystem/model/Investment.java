package com.rbi.loyaltysystem.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Investment {

    private long id;

    private String description;

    private double balance;

    private long customerId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate date;
}
