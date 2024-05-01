package com.vsrka.exchange.connectDB;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
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
    private final String driverName = "org.sqlite.JDBC";

    public PatchExhange() {
        try {
            URL resource = PatchExhange.class.getClassLoader().getResource("servlet.db");
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

    public Rate updateRate(String code1, String code2, String rate) throws Exception {
        GetCurrencies getCurrencies = new GetCurrencies();
        Rate currRate = getCurrencies.getExchangeRatePair(code1, code2);

        if(currRate != null) {
            String query = "UPDATE ExchangeRates SET Rate=" + rate + " WHERE ID=" + currRate.getId();
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
