package com.trivago.searchcore.casestudy.deals.create.currencyconversion;

public class CurrencyNotFoundException extends IllegalArgumentException {
    public CurrencyNotFoundException(String s) {
        super(s);
    }
}
