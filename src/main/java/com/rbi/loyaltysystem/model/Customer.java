package com.rbi.loyaltysystem.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbi.loyaltysystem.exception.TransactionalException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.rbi.loyaltysystem.util.Utils.LOWER_BALANCE;
import static com.rbi.loyaltysystem.util.Utils.NEGATIVE_AMOUNT;

@Getter
@Setter
public class Customer {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    private String name;

    private double balance;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Point> points;

    public void deposit(double amount) {
        if (amount < 0) {
            throw new TransactionalException(NEGATIVE_AMOUNT);
        }

        balance += amount;
    }

    public void withdraw(double amount) {
        if (balance < amount) {
            throw new TransactionalException(LOWER_BALANCE);
        }

        balance -= amount;
    }

    public void addPoint(Point point) {
        if (points == null) {
            points = new ArrayList<>();
        }
        points.add(point);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id &&
                Double.compare(customer.balance, balance) == 0 &&
                Objects.equals(name, customer.name) &&
                Objects.equals(points, customer.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, balance, points);
    }
}
