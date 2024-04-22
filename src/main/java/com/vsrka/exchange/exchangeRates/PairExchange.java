package com.vsrka.exchange.exchangeRates;
import com.vsrka.exchange.currencies.Currency;

public class PairExchange {
    Currency baseCurrency;
    Currency targetCurrency;
    double rate;
    double amount;
    double convertedAmount;

    public PairExchange(Currency baseCurrency, Currency targetCurrency, double rate, double amount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = amount * rate;
    }
}
