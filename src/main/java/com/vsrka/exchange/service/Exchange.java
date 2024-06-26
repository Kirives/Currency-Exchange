package com.vsrka.exchange.service;

import com.vsrka.exchange.exchangeRates.*;
import com.vsrka.exchange.connectDB.GetCurrencies;

import java.sql.SQLException;

public class Exchange {

    public PairExchange getExchange(String baseCurrency,String targetCurrency,String amountString ) throws Exception {
        int amount = Integer.parseInt(amountString);
        GetCurrencies getCurrencies = new GetCurrencies();

        try {
            Rate rateCurr = getCurrencies.getExchangeRatePair(baseCurrency,targetCurrency);
            if(rateCurr == null) {
                //Поиск через реверсию валют
                rateCurr = getCurrencies.getExchangeRatePair(targetCurrency,baseCurrency);
                if(rateCurr != null) {
                    PairExchange pairExchange = new PairExchange(rateCurr.getBaseCurrency(),rateCurr.getTargetCurrency(),1/rateCurr.getRate(), amount);
                    getCurrencies.closeConnection();
                    return pairExchange;
                }else{
                    //через USD
                    Rate rateCurrFirst = getCurrencies.getExchangeRatePair(baseCurrency,"USD");
                    Rate rateCurrSecond = getCurrencies.getExchangeRatePair(targetCurrency,"USD");
                    if(rateCurrFirst!=null && rateCurrSecond!=null) {
                        PairExchange pairExchange = new PairExchange(rateCurrFirst.getBaseCurrency(),rateCurrSecond.getBaseCurrency(),rateCurrFirst.getRate()*(1/rateCurrSecond.getRate()), amount);
                        getCurrencies.closeConnection();
                        return pairExchange;
                    }
                    return null;
                }
            }else {
                PairExchange pairExchange = new PairExchange(rateCurr.getBaseCurrency(),rateCurr.getTargetCurrency(),rateCurr.getRate(), amount);
                getCurrencies.closeConnection();
                return pairExchange;
            }
        }catch (SQLException e) {
            throw new Exception("Exchange rate for the pair was not found",e);
        }catch (Exception e) {
            throw new Exception("The database is not responding",e);
        }

        //return null;

    }
}
