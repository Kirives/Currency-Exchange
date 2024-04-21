package com.vsrka.exchange.currencies;

public class Currency {
    private int id;
    private String name;
    private String code;
    private String number;

    public Currency(int id, String name, String code, String number) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.number = number;
    }
}
