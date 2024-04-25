package com.vsrka.exchange.connectDB;

import com.vsrka.exchange.currencies.Currency;
import com.vsrka.exchange.exchangeRates.Rate;

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

    public PostCurrencies() {
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
            String query = "INSERT INTO testjava.ExchangeRates(BaseCurrencyId, TargetCurrencyId, Rate) VALUES (\"" + currency1.getId() + "\", \"" + currency2.getId() + "\", \"" + rate + "\")";
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
