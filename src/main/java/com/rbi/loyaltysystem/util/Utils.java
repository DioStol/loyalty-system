package com.rbi.loyaltysystem.util;

import com.rbi.loyaltysystem.dto.InvestmentDto;
import com.rbi.loyaltysystem.dto.PointDto;
import com.rbi.loyaltysystem.dto.TransactionDto;
import com.rbi.loyaltysystem.model.Investment;
import com.rbi.loyaltysystem.model.Point;
import com.rbi.loyaltysystem.model.Transaction;

import java.util.List;


public class Utils {

    public static final String NEGATIVE_AMOUNT = "Can not transfer negative amount";

    public static final String NOT_AVAILABLE_CUSTOMERS = "There are not available customers";

    public static final String CUSTOMER_DOES_NOT_EXISTS = "Customer does not exists";

    public static final String LOWER_BALANCE = "Your balance is lower than the given amount";

    public static final String NOT_AVAILABLE_TRANSACTIONS = "There are not available transactions";

    public static final String TRANSACTION_DOES_NOT_EXISTS = "Transaction does not exists";

    public static final String NOT_AVAILABLE_POINTS = "There are not available points";

    public static final String TRANSFER_TO_SAME_ACCOUNT = "Can not transfer money to the same account id";

    public static final String INVESTMENT_DOES_NOT_EXISTS = "Investment does not exists";

    public static InvestmentDto convertInvestmentsToDto(List<Investment> investments) {
        double total = 0;
        for (Investment investment : investments) {
            total += investment.getBalance();
        }
        return new InvestmentDto(investments, total);
    }

    public static PointDto convertPointsToDto(List<Point> points) {
        double total = 0;
        for (Point point : points) {
            total += point.getEarnings();
        }
        return new PointDto(points, total);
    }

    public static TransactionDto convertTransactionsToDto(List<Transaction> transactions) {
        double total = 0;
        for (Transaction transaction : transactions) {
            total += transaction.getAmount();
        }
        return new TransactionDto(transactions, total);
    }
}
