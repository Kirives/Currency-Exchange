package com.vsrka.exchange.connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.vsrka.exchange.currencies.Currency;

public class GetCurrencies {

    final String url = "jdbc:mysql://localhost:3306/testjava";
    final String user = "root";
    final String password = "1133Ov..";
    Statement statement = null;
    Connection connection;

    public GetCurrencies() {

        try{
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            System.out.println("Connection succesfull!");
        }
        catch(Exception ex){
            System.out.println("Connection failed...");

            System.out.println(ex);
        }

        try {
            connection = DriverManager.getConnection(url,user,password);

            statement = connection.createStatement();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Currency> getCurrencies() {

        String query = "SELECT * FROM testjava.Currencies";
        try {
            ResultSet rs = statement.executeQuery(query);
            List<Currency> currencies = new ArrayList<>();
            while (rs.next()) {
               Currency currency = new Currency(rs.getInt("ID"),rs.getString("Code"),rs.getString("FullName"), rs.getString("Number") );
               currencies.add(currency);
            }
            return currencies;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public Currency getCurrency(String code) {
        String query = "SELECT * FROM testjava.Currencies WHERE Code = \"" + code + "\"";
        try {
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
                System.out.println(rs.getInt("ID"));
                return new Currency(rs.getInt("ID"),rs.getString("Code"),rs.getString("FullName"), rs.getString("Number") );
            }
            return null;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
