package com.trivago.searchcore.casestudy.deals.create.pricecosmetics;

import java.util.Arrays.*;
import org.springframework.stereotype.Service;

@Service
public class DummyCurrencyDisplayConfigurationProvider implements CurrencyDisplayConfigurationProvider {
    private static final CurrencyDisplayConfiguration defaultConfiguration = CurrencyDisplayConfiguration.UNITS;

    /**
     * In the old main service, the CurrencyDisplayConfiguration is passed as a request parameter to the search.
     * In our current implementation, we want to have it based on `currency` and `platformCode`. The available data
     * streams do not support this, yet. We implemented a very crude hard-coded-dummy variant. It will only
     * differ from the default case for IDR and VND.
     *
     * @param currency     identifier of the currency, e.g. "EUR" or "USD"
     * @param platformCode ignored
     * @return defaultConfiguration or HUNDREDS_OF_UNITS for "VND" and "IDR"
     */
    @Override
    public CurrencyDisplayConfiguration getCurrencyDisplayConfiguration(String currency, String platformCode) {
        switch (currency.toUpperCase()) {
            case "VND":
            case "IDR":
                return CurrencyDisplayConfiguration.HUNDREDS_OF_UNITS;
            default:
                return defaultConfiguration;

        }
    }
}
