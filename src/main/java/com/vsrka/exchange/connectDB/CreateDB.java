package com.vsrka.exchange.connectDB;

import java.sql.*;

public class CreateDB {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:C:\\Users\\User\\Desktop\\Java\\JavaEE\\Exchange\\all.db";
        try (Connection connection = DriverManager.getConnection(url)){
            Statement statement = connection.createStatement(); // для передачи команд

            String sql = "SELECT * FROM Currencies";

            ResultSet res = statement.executeQuery(sql);

            while (res.next()) {
                System.out.println("<p>" + res.getString(1) + "</p>");
            }

            System.out.println("Таблица успешно создана.");

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
