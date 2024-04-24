package com.vsrka.exchange.connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import com.vsrka.exchange.currencies.Currency;
import com.vsrka.exchange.exchangeRates.Rate;


public class PatchExhange {

    final String url = "jdbc:mysql://localhost:3306/testjava";
    final String user = "root";
    final String password = "1133Ov..";
    Statement statement = null;
    Connection connection;

    public PatchExhange() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            System.out.println("Connection succesfull!");
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Rate updateRate(String code1,String code2, String rate){
        GetCurrencies getCurrencies = new GetCurrencies();
        Rate currRate = getCurrencies.getExchangeRatePair(code1,code2);
        if(currRate!=null){
            String query ="UPDATE testjava.ExchangeRates SET Rate="+rate+" WHERE ID="+currRate.getId();
            try {
                statement.executeUpdate(query);
                return getCurrencies.getExchangeRatePair(code1,code2);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
