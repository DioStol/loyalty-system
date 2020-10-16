package com.rbi.loyaltysystem.util;

import com.rbi.loyaltysystem.dto.InvestmentDto;
import com.rbi.loyaltysystem.dto.PointDto;
import com.rbi.loyaltysystem.dto.TransactionDto;
import com.rbi.loyaltysystem.model.Investment;
import com.rbi.loyaltysystem.model.Point;
import com.rbi.loyaltysystem.model.Transaction;

import java.util.List;


public class Utils {

    public static InvestmentDto convertInvestmentsToDto(List<Investment> investments){
        double total = 0;
        for(Investment investment : investments){
            total += investment.getBalance();
        }
        return new InvestmentDto(investments, total);
    }

    public static PointDto convertPointsToDto(List<Point> points){
        double total = 0;
        for(Point point : points){
            total += point.getEarnings();
        }
        return new PointDto(points, total);
    }

    public static TransactionDto convertTransactionsToDto(List<Transaction> transactions){
        double total = 0;
        for (Transaction transaction : transactions) {
            total += transaction.getAmount();
        }
        return new TransactionDto(transactions, total);
    }
}
