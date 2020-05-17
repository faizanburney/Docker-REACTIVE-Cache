package com.trivago.searchcore.casestudy.deals.create;

import com.trivago.searchcore.casestudy.deals.create.pricecalculation.PlatformPriceCalculator;
import com.trivago.searchcore.casestudy.deals.create.pricecalculation.StayDurationCalculator;
import com.trivago.searchcore.casestudy.deals.create.pricecosmetics.DisplayPriceCalculator;
import com.trivago.searchcore.casestudy.deals.models.Deal;
import com.trivago.searchcore.casestudy.deals.models.DealParameters;
import com.trivago.searchcore.casestudy.deals.models.SingleItemDealsRequest;
import com.trivago.searchcore.casestudy.deals.models.SingleItemDealsResponse;
import com.trivago.searchcore.casestudy.prices.models.ItemPrices;
import com.trivago.searchcore.casestudy.prices.models.Price;
import com.trivago.searchcore.casestudy.prices.models.PriceFetcherResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DealCreationService {
    
    @Autowired
    private DisplayPriceCalculator displayPriceCalculator;

    @Autowired
    private StayDurationCalculator stayDurationCalculator;

    @Autowired
    private PlatformPriceCalculator platformPriceCalculator;

    /**
     * Converts {@link ItemPrices} in {@link PriceFetcherResponse} for given {@link SingleItemDealsRequest} to
     * corresponding {@link SingleItemDealsResponse}
     *
     * No filtering of any kind.
     *
     * @param priceFetcherResponse
     * @return
     */
    public SingleItemDealsResponse createDealsFromPrices(SingleItemDealsRequest singleItemDealsRequest,
        PriceFetcherResponse priceFetcherResponse) {
        log.debug("createDealsFromPrices for item ID {}, {} prices, status {}",
            singleItemDealsRequest.getItemId(),
            priceFetcherResponse.getItemPrices().getPrices().size(),
            priceFetcherResponse.getStatus()
        );
        log.trace("createDealsFromPrices:: {} -- {}", singleItemDealsRequest, priceFetcherResponse);
        if (priceFetcherResponse.getStatus().equals(PriceFetcherResponse.STATUS_COMPLETE)) {
            ItemPrices itemPrices = priceFetcherResponse.getItemPrices();

            Set<Deal> deals = itemPrices.getPrices()
                .stream()
                .map(price -> createDealFromPrice(price, singleItemDealsRequest.getDealParameters()))
                .collect(Collectors.toSet());

            return SingleItemDealsResponse.builder()
                .itemId(itemPrices.getItemId())
                .deals(deals)
                .status(priceFetcherResponse.getStatus())
                .build();
        } else {
            return SingleItemDealsResponse.builder()
                .itemId(singleItemDealsRequest.getItemId())
                .deals(Collections.emptySet()) // To avoid NPE in replacement service
                .status(priceFetcherResponse.getStatus())
                .build();
        }
    }

    public Deal createDealFromPrice(Price price, DealParameters dealParameters) {

        final String requestCurrencyCode = dealParameters.getCurrencyCode();
        final String requestPlatformCode = dealParameters.getPlatformCode();

        final double pricePerNightInRequestCurrencyCents = platformPriceCalculator.calculatePricePerNight(price,
            requestPlatformCode,
            requestCurrencyCode
        );

        final int numberOfNights = stayDurationCalculator.calculateNumberOfNights(dealParameters.getCheckInDate(),
            dealParameters.getCheckOutDate()
        );
        final double pricePerStayInRequestCurrencyCents = pricePerNightInRequestCurrencyCents * numberOfNights;

        final int pricePerNightInRequestCurrency =
            (int) displayPriceCalculator.calculateDisplayPrice(pricePerNightInRequestCurrencyCents,
            requestCurrencyCode,
            requestPlatformCode
        );

        final double pricePerStayInRequestCurrency =
            displayPriceCalculator.calculateDisplayPrice(pricePerStayInRequestCurrencyCents,
            requestCurrencyCode,
            requestPlatformCode
        );

        // We redo the price calculation here in order to avoid rounding errors, when calculating the eurocent price
        final int pricePerNightInEuroCents = (int) platformPriceCalculator.calculatePricePerNight(price,
            requestPlatformCode,
            "EUR"
        );

        return Deal.builder()
            .partnerId(price.getPartnerId())
            .pricePerNight(pricePerNightInRequestCurrency)
            .pricePerStay(pricePerStayInRequestCurrency)
            .eurocentPricePerNight(pricePerNightInEuroCents)
            .build();
    }
}
