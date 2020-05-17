package com.trivago.searchcore.casestudy.prices.fetcher;

import com.trivago.searchcore.casestudy.deals.create.DealCreationService;
import com.trivago.searchcore.casestudy.deals.models.SingleItemDealsRequest;
import com.trivago.searchcore.casestudy.prices.models.PriceFetcherRequest;
import com.trivago.searchcore.casestudy.prices.models.PriceFetcherResponse;

public interface ItemPricesProcessor {

    /**
     * The ItemPrices received from PriceFetcher should be processed and converted to corresponding ItemDeals. This
     * should use {@link DealCreationService} for the conversion.
     *
     * @param singleItemDealsRequest
     * @param priceFetcherResponse
     */
    void processItemPrices(SingleItemDealsRequest singleItemDealsRequest, PriceFetcherResponse priceFetcherResponse);

    /**
     * Handle finished PriceFetcherRequest
     *
     * @param priceFetcherRequest
     */
    void onCompleted(PriceFetcherRequest priceFetcherRequest);

    /**
     * Handle error for PriceFetcherRequest
     *
     * @param priceFetcherRequest
     * @param throwable
     */
    void onError(PriceFetcherRequest priceFetcherRequest, Throwable throwable);
}
