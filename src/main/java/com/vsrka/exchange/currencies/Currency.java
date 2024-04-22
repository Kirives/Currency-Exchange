package com.vsrka.exchange.currencies;

public class Currency {
    private int id;
    private String fullName;
    private String code;
    private String number;

    public Currency(int id,String code, String fullName, String number) {
        this.id = id;
        this.fullName = fullName;
        this.code = code;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCode() {
        return code;
    }

    public String getNumber() {
        return number;
    }
}
