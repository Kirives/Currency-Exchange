package com.vsrka.exchange.currenciesAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsrka.exchange.connectDB.GetCurrencies;
import com.vsrka.exchange.errors.Error;
import com.vsrka.exchange.exchangeRates.PairExchange;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

import com.vsrka.exchange.service.Exchange;
import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet(name = "exchange", value = "/exchange")
public class exchange extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        String baseCurrency = request.getParameter("from");
        String targetCurrency = request.getParameter("to");
        String amount = request.getParameter("amount");
        if (baseCurrency != null && targetCurrency != null && amount != null) {
            try {
                Exchange exchange = new Exchange();
                PairExchange pairExchange =exchange.getExchange(baseCurrency, targetCurrency, amount);
                if(pairExchange != null) {
                    response.setStatus(SC_OK);
                    objectMapper.writeValue(out, pairExchange);
                }else{
                    response.setStatus(SC_NOT_FOUND);
                    objectMapper.writeValue(out, new Error(SC_NOT_FOUND,"Exchange rate for the pair was not found"));
                }

            }catch (Exception e) {
                response.setStatus(SC_INTERNAL_SERVER_ERROR);
                objectMapper.writeValue(out, new Error(SC_INTERNAL_SERVER_ERROR,e.getMessage()));
            }
        }else{
            response.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(out,new Error(SC_BAD_REQUEST,"The address does not contain the required parameters"));
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}