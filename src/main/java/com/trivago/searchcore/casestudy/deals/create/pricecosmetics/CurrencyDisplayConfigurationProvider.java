package com.trivago.searchcore.casestudy.deals.create.pricecosmetics;

public interface CurrencyDisplayConfigurationProvider {
    /**
     * Provides a display configuration object for a given currency and platform
     *
     * @param currency     identifier of the currency, e.g. "EUR" or "USD"
     * @param platformCode identifier of the platform, e.g. "com" or "de"
     * @return a currency display configuration object
     * @throws IllegalArgumentException if the currency or platform are unknown
     */
    CurrencyDisplayConfiguration getCurrencyDisplayConfiguration(String currency, String platformCode);
}
