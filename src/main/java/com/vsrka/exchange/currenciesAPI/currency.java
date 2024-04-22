package com.vsrka.exchange.currenciesAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

import com.vsrka.exchange.connectDB.GetCurrencies;

@WebServlet(name = "currency", value = "/currency/*")
public class currency extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        GetCurrencies getCurrencies = new GetCurrencies();
        String url = request.getRequestURL().toString();
        String [] words = url.split("/");

        out.println("<html>");
        objectMapper.writeValue(out, getCurrencies.getCurrency(words[words.length-1]));
        out.println("</html>");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}