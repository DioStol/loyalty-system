package com.rbi.loyaltysystem.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author Dionysios Stolis 10/15/2020 <dstolis@b-open.com>
 */
@Getter
public class Point {

    private Status status;

    private int amount;

    private LocalDate date;

    private double money;

    private long transactionId;

    public Point(int amount, long transactionId) {
        this.amount = amount;
        this.money = convertToEuro(amount);
        this.date = LocalDate.now();
        this.status = Status.PENDING;
        this.transactionId = transactionId;
    }

    private double convertToEuro(double points) {
        return points * 0.01;
    }

    public void activate(){
        this.status = Status.AVAILABLE;
    }
}
