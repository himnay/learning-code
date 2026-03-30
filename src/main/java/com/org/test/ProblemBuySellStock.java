package com.org.test;

import java.util.List;

public class ProblemBuySellStock {
    void main() {
        var stocks = List.of(3, 2, 1, 5, 3, 1, 1, 7, 9, 1);
        Integer minStock = Integer.MAX_VALUE;
        Integer maxStock = Integer.MIN_VALUE;
        int profit = 0;
        int maxProfit = Integer.MIN_VALUE;
        for(int stock : stocks) {
            if(stock < minStock) {
                minStock = stock;
            }
            profit = stock - minStock;
            if(profit > maxProfit) {
                maxProfit = profit;
                maxStock = stock;
            }
        }
        System.out.println("Min Stock : " + minStock);
        System.out.println("Max Stock : " + maxStock);
        System.out.println("Max Profit : " + maxProfit);
    }

}
