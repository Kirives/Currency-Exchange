package com.vsrka.exchange.exchangeRates;
import com.vsrka.exchange.currencies.Currency;

public class Rate {
    int id;
    Currency baseCurrency;
    Currency targetCurrency;
    double rate;

    public Rate(int id, Currency baseCurrency, Currency targetCurrency, double rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }
}
