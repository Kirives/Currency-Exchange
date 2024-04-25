package com.vsrka.exchange.connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

    public Rate updateRate(String code1, String code2, String rate) throws Exception {
        GetCurrencies getCurrencies = new GetCurrencies();
        Rate currRate = getCurrencies.getExchangeRatePair(code1, code2);

        if(currRate != null) {
            String query = "UPDATE testjava.ExchangeRates SET Rate=" + rate + " WHERE ID=" + currRate.getId();
            try {
                statement.executeUpdate(query);
                return getCurrencies.getExchangeRatePair(code1, code2);
            } catch (SQLException e) {
                throw new Exception("Exchange rate for the pair was not found",e);
            }catch (Exception e) {
                throw new Exception("The database is not responding",e);
            }
        }else{
            return null;
        }


    }

}
