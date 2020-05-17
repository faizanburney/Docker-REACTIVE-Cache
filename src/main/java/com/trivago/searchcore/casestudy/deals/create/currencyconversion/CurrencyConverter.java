package com.trivago.searchcore.casestudy.deals.create.currencyconversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CurrencyConverter {
    
    // default currency of our application
    // This could be a configuration variable, however there are major implications for the rest of
    // our application. To me, it seems safer to keep this setting hard coded.
    public static final String DEFAULT_CURRENCY = "EUR";

    @Autowired
    private ExchangeRateProvider exchangeRateProvider;

    /**
     * Converts a value from a <code>sourceCurrency</code> to a <code>targetCurrency</code>. If the value is provided in
     * internal format (i.e. one-hundredth-of-...) the output value will be also in internal format.
     *
     * @param value          currency value in abstract unit "one hundredth of currency"
     * @param sourceCurrency identifier of the source currency
     * @param targetCurrency identifier of the target currency
     * @return converted value in "one hundredth of target currency"
     * @throws CurrencyNotFoundException if the currency strings are unknown
     */
    public double convert(double value, String sourceCurrency, String targetCurrency) {

        if (sourceCurrency.equalsIgnoreCase(targetCurrency)) {
            return value;
        }

        // might raise IllegalArgumentException for unknown currencies
        double conversionRate = exchangeRateProvider.getExchangeRate(sourceCurrency, targetCurrency);

        return Math.round(value * conversionRate);

    }

    /**
     * Converts a value in <code>sourceCurrency</code> to our default currency (currently Euros)
     *
     * @param value          currency value in abstract unit "one hundredth of currency"
     * @param sourceCurrency identifier of the source currency
     * @return a price in one hundredth of default currency
     * @throws IllegalArgumentException if the currencyString is unknown
     */
    public double convertToDefaultCurrency(double value, String sourceCurrency) {
        return convert(value, sourceCurrency, DEFAULT_CURRENCY);
    }
}
