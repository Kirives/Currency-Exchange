package com.vsrka.exchange.currenciesAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsrka.exchange.connectDB.GetCurrencies;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

import com.vsrka.exchange.service.Exchange;

@WebServlet(name = "exchange", value = "/exchange")
public class exchange extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        GetCurrencies getCurrencies = new GetCurrencies();
        String baseCurrency = request.getParameter("from");
        String targetCurrency = request.getParameter("to");
        String amount = request.getParameter("amount");
        Exchange exchange = new Exchange();
        objectMapper.writeValue(out, exchange.getExchange(baseCurrency, targetCurrency, amount));


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}