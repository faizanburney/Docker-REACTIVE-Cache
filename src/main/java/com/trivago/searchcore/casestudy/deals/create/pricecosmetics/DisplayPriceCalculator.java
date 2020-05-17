package com.trivago.searchcore.casestudy.deals.create.pricecosmetics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DisplayPriceCalculator {

    // performance optimization: pre-calculated powers of ten
    private static final double[] powersOf10 = {
        1d, 10d, 100d, 1000d, 10000d, 100000d, 1000000d, 10000000d, 100000000d, 1000000000d, 10000000000d, 100000000000d
    };

    @Autowired
    private CurrencyDisplayConfigurationProvider currencyDisplayConfigurationProvider;

    /**
     * Apply rounding, etc. to the given price, using the rules which apply by the given currency and platformCode.
     *
     * @param price        in abstract unit one-hundredth-of-currency.
     * @param currency     on which the request is built
     * @param platformCode identifier for a trivago platform (e.g. DE, COM, ...)
     * @return adjusted price, as to be forwarded to the frontend
     * @throws IllegalArgumentException when currency or platformcode are unknown
     */
    public double calculateDisplayPrice(double price, String currency, String platformCode) {
        // might raise IllegalArgumentException for invalid platform codes
        CurrencyDisplayConfiguration displayConfiguration = currencyDisplayConfigurationProvider
            .getCurrencyDisplayConfiguration(currency,
            platformCode
        );
        int roundDigits = displayConfiguration.getRoundDigits();
        int padDigits = displayConfiguration.getPadDigits();
        CurrencyDisplayConfiguration.RoundingMode roundingMode = displayConfiguration.getRoundingMode();

        if (roundDigits >= powersOf10.length || padDigits >= powersOf10.length) {
            throw new IllegalArgumentException(
                "roundDigits and padDigits must be between 0 and " + (powersOf10.length - 1) + ":" + roundDigits + ", "
                + padDigits);
        }
        double roundingFactor = powersOf10[roundDigits];
        double zeroesPaddingFactor = powersOf10[padDigits];

        switch (roundingMode) {
            case FLOOR:
                return (long) (Math.floor(price / roundingFactor) * zeroesPaddingFactor);
            case CEILING:
                return (long) (Math.ceil(price / roundingFactor) * zeroesPaddingFactor);
            case ROUND:
            default:
                return (long) (Math.round(price / roundingFactor) * zeroesPaddingFactor);
        }
    }
}
