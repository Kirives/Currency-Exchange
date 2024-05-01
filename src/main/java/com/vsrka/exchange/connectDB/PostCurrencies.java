package com.vsrka.exchange.connectDB;

import com.vsrka.exchange.currencies.Currency;
import com.vsrka.exchange.exchangeRates.Rate;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.vsrka.exchange.errors.Error;
import static jakarta.servlet.http.HttpServletResponse.*;


public class PostCurrencies {

    final String url = "jdbc:mysql://localhost:3306/testjava";
    final String user = "root";
    final String password = "1133Ov..";
    Statement statement = null;
    Connection connection;
    private final String driverName = "org.sqlite.JDBC";

    public PostCurrencies() {
        try {
            URL resource = PostCurrencies.class.getClassLoader().getResource("servlet.db");
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

    //Оставю как пример для учёбы
//    public Object postCurrencies(String code, String name, String number) {
//        GetCurrencies getCurrencies = new GetCurrencies();
//        String query = "INSERT INTO Currencies (Code, FullName, Number) values (\"" + code + "\", \"" + name + "\", \"" + number + "\")";
//        try {
//            statement.executeUpdate(query);
//            Currency currency = getCurrencies.getCurrency(code);
//            getCurrencies.closeConnection();
//            return currency;
//        } catch (SQLException ex) {
//            return new Error(SC_CONFLICT,"Currency with this code already exists");
//        }catch (Exception e){
//            return new Error(SC_INTERNAL_SERVER_ERROR,"The database is not responding");
//        }
//    }

    public Currency postCurrenciesv2(String code, String name, String number) throws Exception {
        GetCurrencies getCurrencies = new GetCurrencies();
        String query = "INSERT INTO Currencies (Code, FullName, Number) values (\"" + code + "\", \"" + name + "\", \"" + number + "\")";
        try {
            statement.executeUpdate(query);
            Currency currency = getCurrencies.getCurrency(code);
            getCurrencies.closeConnection();
            return currency;
        } catch (SQLException ex) {
            throw new Exception("Currency with this code already exists",ex);
        }catch (Exception ex){
            throw new Exception("The database is not responding",ex);
        }
    }

    public Rate postRate(String code1, String code2, String rate) throws Exception {
        GetCurrencies getCurrencies = new GetCurrencies();

        Currency currency1 = getCurrencies.getCurrency(code1);
        Currency currency2 = getCurrencies.getCurrency(code2);


        if (currency1 != null && currency2 != null) {
            String query = "INSERT INTO ExchangeRates(BaseCurrencyId, TargetCurrencyId, Rate) VALUES (\"" + currency1.getId() + "\", \"" + currency2.getId() + "\", \"" + rate + "\")";
            try {
                statement.executeUpdate(query);
                Rate rateCurr = getCurrencies.getExchangeRatePair(code1, code2);
                getCurrencies.closeConnection();
                return rateCurr;
            } catch (SQLException e) {
                throw new Exception("A currency pair with this code already exists",e);
            }catch (Exception e){
                throw new Exception("The database is not responding",e);
            }
        }else {
            throw new Exception("One (or both) currencies from a currency pair do not exist in the database");
        }



    }




}
