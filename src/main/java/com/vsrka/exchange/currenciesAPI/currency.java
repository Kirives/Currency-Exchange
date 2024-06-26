package com.vsrka.exchange.currenciesAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsrka.exchange.currencies.Currency;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

import com.vsrka.exchange.connectDB.GetCurrencies;
import com.vsrka.exchange.errors.Error;
import static jakarta.servlet.http.HttpServletResponse.*;


@WebServlet(name = "currency", value = "/currency/*")
public class currency extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        GetCurrencies getCurrencies = new GetCurrencies();
        String url = request.getRequestURL().toString();
        String [] words = url.split("/");

        if(words[words.length-1].equals("currency")){
            response.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(out,new Error(SC_BAD_REQUEST,"Currency for the search is not specified"));
        }else{
            try {
                Currency currency= getCurrencies.getCurrency(words[words.length-1]);
                if(currency != null){
                    response.setStatus(SC_OK);
                    objectMapper.writeValue(out,currency);
                }else{
                    response.setStatus(SC_NOT_FOUND);
                    objectMapper.writeValue(out,new Error(SC_NOT_FOUND,"Currency is not in the database"));
                }
            }catch (Exception e){
                objectMapper.writeValue(out,new Error(SC_INTERNAL_SERVER_ERROR,"The database is not responding"));
            }
            getCurrencies.closeConnection();
        }
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}