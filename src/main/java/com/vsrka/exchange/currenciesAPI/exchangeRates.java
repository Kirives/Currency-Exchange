package com.vsrka.exchange.currenciesAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsrka.exchange.connectDB.PostCurrencies;
import com.vsrka.exchange.errors.Error;
import com.vsrka.exchange.exchangeRates.Rate;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.vsrka.exchange.connectDB.GetCurrencies;
import static jakarta.servlet.http.HttpServletResponse.*;
import com.vsrka.exchange.exchangeRates.Rate;

@WebServlet(name = "exchangeRates", value = "/exchangeRates")
public class exchangeRates extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        GetCurrencies getCurrencies = new GetCurrencies();

        try {
            List<Rate> rates = getCurrencies.getExchangeRates();
            response.setStatus(SC_OK);
            objectMapper.writeValue(out, rates);
        }catch (Exception e) {
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
        }
        getCurrencies.closeConnection();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        PostCurrencies postCurrencies = new PostCurrencies();

        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");
        String rate = request.getParameter("rate");

        if (baseCurrencyCode != null && targetCurrencyCode != null && rate != null) {
            try {
                Rate rateCurr = postCurrencies.postRate(baseCurrencyCode, targetCurrencyCode, rate);
                response.setStatus(SC_CREATED);
                objectMapper.writeValue(out, rateCurr);
            } catch (Exception e) {
                switch (e.getMessage()) {
                    case "A currency pair with this code already exists":
                        response.setStatus(SC_CONFLICT);
                        objectMapper.writeValue(out, new Error(SC_CONFLICT, "Currency pair with this code already exists"));
                        break;
                    case "The database is not responding":
                        response.setStatus(SC_INTERNAL_SERVER_ERROR);
                        objectMapper.writeValue(out, new Error(SC_INTERNAL_SERVER_ERROR, "Database is not responding"));
                        break;
                    case "One (or both) currencies from a currency pair do not exist in the database":
                        response.setStatus(SC_NOT_FOUND);
                        objectMapper.writeValue(out, new Error(SC_NOT_FOUND, "One (or both) currencies from a currency pair do not exist in the database"));
                        break;
                }
            }
        } else {
            response.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(out,new Error(SC_BAD_REQUEST,"The request body is missing"));
        }
    }
}