package com.trivago.searchcore.casestudy.deals.create.currencyconversion;

import org.springframework.stereotype.Service;


/**
 * CASE-STUDY: This class is a placeholder endpoint - we would normally
 * have something more complicated here.
 *
 * Feel free to ignore this class, no need to change something here.
 */
@Service
public class DummyExchangeRateProvider implements ExchangeRateProvider {
    @Override
    public double getExchangeRate(String sourceCurrency, String targetCurrency) {
        return 1D;
    }
}
