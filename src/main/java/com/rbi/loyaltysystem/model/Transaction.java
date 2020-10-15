package com.rbi.loyaltysystem.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Transaction {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate date;

    private long senderId;

    private long recipientId;

    private double amount;
}
