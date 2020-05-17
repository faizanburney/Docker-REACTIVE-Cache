package com.trivago.searchcore.casestudy.deals.create.pricecalculation;

import com.trivago.searchcore.casestudy.deals.create.currencyconversion.CurrencyConverter;
import com.trivago.searchcore.casestudy.prices.models.Price;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlatformPriceCalculator {

    @Autowired
    private CurrencyConverter currencyConverter;

    /**
     * Given a specific price, calculate the price per night in the target currency according to the rules of the given
     * platform. This is mainly done when we get the separate price components and need to decide, whether to add VAT,
     * partnerFees, etc. to the net price.
     *
     * @param price              the Price object to examine
     * @param platformCode       defines, which platform rules to apply
     * @param targetCurrencyCode the currency in which this price should be returned
     * @return a calculated price (in Price Components currency) which takes into account, which price components are
     * used for the given platform
     */
    public double calculatePricePerNight(Price price, String platformCode, String targetCurrencyCode) {

        final double calculatedPrice;
        final String priceCurrencyCode;
        calculatedPrice = price.getCalculatedPrice();
        priceCurrencyCode = price.getCurrencyCode();
        return currencyConverter.convert(calculatedPrice, priceCurrencyCode, targetCurrencyCode);
    }

}

