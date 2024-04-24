package com.vsrka.exchange.currenciesAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import com.vsrka.exchange.connectDB.GetCurrencies;
import com.vsrka.exchange.exchangeRates.Rate;

@WebServlet(name = "exchangeRatePair", value = "/exchangeRate/*")
public class exchangeRatePair extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        GetCurrencies getCurrencies = new GetCurrencies();
        String url = request.getRequestURL().toString();
        String [] words = url.split("/");
        String pair = words[words.length - 1];
        String firstCurr="";
        String secondCurr=pair;
        for(char ch : pair.toCharArray()) {
            firstCurr+=ch;
            secondCurr=secondCurr.substring(1);
            Rate rate = getCurrencies.getExchangeRatePair(firstCurr,secondCurr);
            if(rate!=null) {
                getCurrencies.closeConnection();
                objectMapper.writeValue(out, rate);
                break;
            }
        }
        getCurrencies.closeConnection();


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}