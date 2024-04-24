package com.vsrka.exchange.connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

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

    public void postCurrencies(String code,String name,String number) {

        GetCurrencies getCurrencies = new GetCurrencies();
        if(getCurrencies.getCurrency(code)==null){
            String quary = "INSERT INTO Currencies (Code, FullName, Number) values (\""+code+"\", \""+name+"\", \""+number+"\")";
            try {
                statement.executeUpdate(quary);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println(name+" "+code+" "+number);
    }
}
