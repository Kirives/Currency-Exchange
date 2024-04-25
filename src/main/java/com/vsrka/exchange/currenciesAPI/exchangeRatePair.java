package com.vsrka.exchange.currenciesAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsrka.exchange.connectDB.PatchExhange;
import com.vsrka.exchange.errors.Error;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import com.vsrka.exchange.connectDB.GetCurrencies;
import com.vsrka.exchange.exchangeRates.Rate;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet(name = "exchangeRatePair", value = "/exchangeRate/*")
public class exchangeRatePair extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        GetCurrencies getCurrencies = new GetCurrencies();
        String url = request.getRequestURL().toString();
        String[] words = url.split("/");
        String pair = words[words.length - 1];

        if (pair.equals("exchangeRate")) {
            response.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(out, new Error(SC_BAD_REQUEST, "Pair currency codes are missing from the address"));
        } else {
            String firstCurr = "";
            String secondCurr = pair;
            try {
                for (char ch : pair.toCharArray()) {
                    firstCurr += ch;
                    secondCurr = secondCurr.substring(1);
                    Rate rate = getCurrencies.getExchangeRatePair(firstCurr, secondCurr);
                    if (rate != null) {
                        getCurrencies.closeConnection();
                        objectMapper.writeValue(out, rate);
                        break;
                    }
                }
                response.setStatus(SC_NOT_FOUND);
                objectMapper.writeValue(out, new Error(SC_NOT_FOUND, "Exchange rate for the pair was not found"));
            } catch (Exception e) {
                response.setStatus(SC_INTERNAL_SERVER_ERROR);
                objectMapper.writeValue(out, new Error(SC_INTERNAL_SERVER_ERROR, "The database is not responding"));
            }

        }
        getCurrencies.closeConnection();

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        PatchExhange patchExhange = new PatchExhange();
        String url = request.getRequestURL().toString();
        String[] words = url.split("/");
        String pair = words[words.length - 1];
        if(pair.equals("exchangeRate")) {
            response.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(out, new Error(SC_BAD_REQUEST, "Pair currency codes are missing from the address"));
        }else{
            String firstCurr = "";
            String secondCurr = pair;
            String rateParametr = request.getReader().readLine();
            if (rateParametr.contains("rate")) {
                String rate = rateParametr.substring(5);
                try {
                    for (char ch : pair.toCharArray()) {
                        firstCurr += ch;
                        secondCurr = secondCurr.substring(1);
                        Rate rateCurr = patchExhange.updateRate(firstCurr, secondCurr, rate);
                        if (rateCurr != null) {
                            response.setStatus(SC_OK);
                            objectMapper.writeValue(out, rateCurr);
                            return;
                        }
                    }
                } catch (Exception e) {
                    if(e.getMessage().equals("Exchange rate for the pair was not found")){
                        response.setStatus(SC_NOT_FOUND);
                        objectMapper.writeValue(out, new Error(SC_NOT_FOUND, "Exchange rate for the pair was not found"));
                    }else{
                        response.setStatus(SC_INTERNAL_SERVER_ERROR);
                        objectMapper.writeValue(out, new Error(SC_INTERNAL_SERVER_ERROR, "The database is not responding"));
                    }
                }
                response.setStatus(SC_NOT_FOUND);
                objectMapper.writeValue(out, new Error(SC_NOT_FOUND,"Exchange rate for the pair was not found"));
            } else {
                response.setStatus(SC_BAD_REQUEST);
                objectMapper.writeValue(out, new Error(SC_BAD_REQUEST, "Pair currency codes are missing from the address"));
            }
        }
    }
}