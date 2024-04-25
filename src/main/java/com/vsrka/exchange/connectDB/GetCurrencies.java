package com.vsrka.exchange.connectDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.vsrka.exchange.currencies.Currency;
import com.vsrka.exchange.exchangeRates.*;

public class GetCurrencies {

    final String url = "jdbc:mysql://localhost:3306/testjava";
    final String user = "root";
    final String password = "1133Ov..";
    Statement statement = null;
    Connection connection;

    public GetCurrencies() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            System.out.println("Connection succesfull!");
        } catch (Exception ex) {
            System.out.println("Connection failed...");

            System.out.println(ex);
        }
        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Currency> getCurrencies() throws SQLException {

        String query = "SELECT * FROM testjava.Currencies";
        ResultSet rs = statement.executeQuery(query);
        List<Currency> currencies = new ArrayList<>();
        while (rs.next()) {
            Currency currency = new Currency(rs.getInt("ID"), rs.getString("Code"), rs.getString("FullName"), rs.getString("Number"));
            currencies.add(currency);
        }
        return currencies;
    }

    public Currency getCurrency(String code) throws Exception{
        String query = "SELECT * FROM testjava.Currencies WHERE Code = \"" + code + "\"";
        try {
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                System.out.println(rs.getInt("ID"));
                return new Currency(rs.getInt("ID"), rs.getString("Code"), rs.getString("FullName"), rs.getString("Number"));
            }
            //нуль нормально, поом отдадим 404 ошибку
            return null;
        } catch (Exception e) {
            throw new Exception("The database is not responding",e);
        }
    }

    private Currency getCurrencyFromID(int id) {
        String query = "SELECT * FROM testjava.Currencies WHERE ID = " + id;
        try {
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                System.out.println(rs.getInt("ID"));
                return new Currency(rs.getInt("ID"), rs.getString("Code"), rs.getString("FullName"), rs.getString("Number"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Rate> getExchangeRates() throws Exception {
        String query = "SELECT * FROM testjava.ExchangeRates";
        try {
            ResultSet rs = statement.executeQuery(query);
            List<Rate> rates = new ArrayList<>();
            GetCurrencies currencies = new GetCurrencies();
            while (rs.next()) {
                int ID = (rs.getInt("ID"));
                int BaseID = (rs.getInt("BaseCurrencyId"));
                int TargetID = (rs.getInt("TargetCurrencyId"));
                Double rateID = (rs.getDouble("Rate"));
                Currency currency1 = currencies.getCurrencyFromID(BaseID);
                Currency currency2 = currencies.getCurrencyFromID(TargetID);
                Rate rate = new Rate(ID, currency1, currency2, rateID);
                rates.add(rate);
            }
            currencies.closeConnection();
            return rates;
        } catch (Exception e) {
            throw new Exception("The database is not responding",e);
        }
    }

    public Rate getExchangeRatePair(String code1, String code2) throws Exception {
        GetCurrencies currencies = new GetCurrencies();
        Currency currency1 = currencies.getCurrency(code1);
        Currency currency2 = currencies.getCurrency(code2);
        try {
            if (currency1 != null && currency2 != null) {
                Rate rate = currencies.getExchangeRatePairFromID(currency1, currency2);
                currencies.closeConnection();
                return rate;
            }
            currencies.closeConnection();
            return null;
        }catch (Exception e) {
            throw new Exception("The database is not responding",e);
        }


    }

    private Rate getExchangeRatePairFromID(Currency currency1, Currency currency2) {
        String query = "SELECT * FROM testjava.ExchangeRates WHERE BaseCurrencyId = " + currency1.getId() + " AND TargetCurrencyId = " + currency2.getId();
        try {
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                return new Rate(rs.getInt("ID"), currency1, currency2, rs.getDouble("Rate"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return null;
    }


    public void closeConnection() {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
