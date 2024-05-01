package com.vsrka.exchange.connectDB;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
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
    private final String driverName = "org.sqlite.JDBC";

    public GetCurrencies() {
        try {
            URL resource = GetCurrencies.class.getClassLoader().getResource("servlet.db");
            String path = null;
            try {
                path = new File(resource.toURI()).getAbsolutePath();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            Class.forName(driverName);
            connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", path));
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Currency> getCurrencies() throws SQLException {

        String query = "SELECT * FROM Currencies";
        ResultSet rs = statement.executeQuery(query);
        List<Currency> currencies = new ArrayList<>();
        while (rs.next()) {
            Currency currency = new Currency(rs.getInt("ID"), rs.getString("Code"), rs.getString("FullName"), rs.getString("Number"));
            currencies.add(currency);
        }
        return currencies;
    }

    public Currency getCurrency(String code) throws Exception{
        String query = "SELECT * FROM Currencies WHERE Code = \"" + code + "\"";
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
        String query = "SELECT * FROM Currencies WHERE ID = " + id;
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
        String query = "SELECT * FROM ExchangeRates";
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
        String query = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId = " + currency1.getId() + " AND TargetCurrencyId = " + currency2.getId();
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
