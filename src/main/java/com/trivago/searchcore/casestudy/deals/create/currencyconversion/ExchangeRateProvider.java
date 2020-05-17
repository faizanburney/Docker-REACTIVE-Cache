package com.trivago.searchcore.casestudy.deals.create.currencyconversion;


public interface ExchangeRateProvider {
    /**
     * Provides the exchange rate from <code>sourceCurrency</code> to <code>targetCurrency</code>
     *
     * @param sourceCurrency identifier for the source currency
     * @param targetCurrency identifier for the target currency
     * @return exchange rate in the unit <code>targetCurrency / sourceCurrency</code>
     * @throws CurrencyNotFoundException if either sourceCurrency or targetCurrency is unknown
     */
    double getExchangeRate(String sourceCurrency, String targetCurrency) throws CurrencyNotFoundException;

}
