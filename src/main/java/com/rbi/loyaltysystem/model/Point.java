package com.rbi.loyaltysystem.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Point {

    private Status status;

    private int amount;

    private LocalDate date;

    private double earnings;

    private long transactionId;

    public Point(double transactionAmount, long transactionId) {
        this.amount = calculatePoints(transactionAmount);
        this.earnings = convertToEuro(amount);
        this.date = LocalDate.now();
        this.status = Status.PENDING;
        this.transactionId = transactionId;
    }

    private double convertToEuro(double points) {
        return points * 0.01;
    }

    public void activate() {
        this.status = Status.AVAILABLE;
    }

    public void deactivate() {
        this.status = Status.SPENT;
        this.earnings = 0;
    }


    private int calculatePoints(double amount) {
        int points;
        if (amount <= 5000) {
            points = (int) amount;
        } else if (amount <= 7500) {
            int x = (int) amount - 5000;
            points = 5000;
            points += x * 2;
        } else {
            points = 10000;
            int x = (int) amount - 7500;
            points += x * 3;
        }
        return points;
    }
}
