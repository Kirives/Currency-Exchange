package com.vsrka.exchange.currenciesAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsrka.exchange.currencies.Currency;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.vsrka.exchange.connectDB.*;

@WebServlet(name = "currencies", value = "/currencies")
public class currencies extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        GetCurrencies getCurrencies = new GetCurrencies();
        objectMapper.writeValue(out, getCurrencies.getCurrencies());//Если не выводит, то нажо сделать геттер
        getCurrencies.closeConnection();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        PostCurrencies postCurrencies = new PostCurrencies();
        GetCurrencies getCurrencies = new GetCurrencies();
        postCurrencies.postCurrencies(request.getParameter("code").toString(),request.getParameter("name").toString(),request.getParameter("number").toString());
        objectMapper.writeValue(out, getCurrencies.getCurrency(request.getParameter("code").toString()));
    }
}