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
import static jakarta.servlet.http.HttpServletResponse.*;
import com.vsrka.exchange.errors.Error;

@WebServlet(name = "currencies", value = "/currencies")
public class currencies extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        GetCurrencies getCurrencies = new GetCurrencies();
        try {
            response.setStatus(SC_OK);
            objectMapper.writeValue(out, getCurrencies.getCurrencies());
        }catch (Exception e) {
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(out, new Error(SC_INTERNAL_SERVER_ERROR,"The database is not responding"));
        }
        getCurrencies.closeConnection();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        PostCurrencies postCurrencies = new PostCurrencies();
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String number = request.getParameter("number");

        //Пробую разные методы отлова ошибок, ещё не знаю какие лучше
        if(code==null||name==null||number==null){
            response.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(out, new Error(SC_BAD_REQUEST,"The request body is missing"));
        }else{
            try {
                Currency currency = postCurrencies.postCurrenciesv2(request.getParameter("code").toString(),request.getParameter("name").toString(),request.getParameter("number").toString());
                response.setStatus(SC_CREATED);
                objectMapper.writeValue(out, currency);
            }catch (Exception e) {
                if (e.getMessage().equals("Currency with this code already exists")){
                    response.setStatus(SC_CONFLICT);
                    objectMapper.writeValue(out, new Error(SC_CONFLICT,"Currency with this code already exists"));
                }else{
                    response.setStatus(SC_INTERNAL_SERVER_ERROR);
                    objectMapper.writeValue(out, new Error(SC_INTERNAL_SERVER_ERROR,"The database is not responding"));
                }
            }
        }
        out.close();
    }
}