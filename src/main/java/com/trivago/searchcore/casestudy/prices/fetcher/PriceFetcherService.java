package com.trivago.searchcore.casestudy.prices.fetcher;

import com.trivago.searchcore.casestudy.prices.models.PriceFetcherRequest;

public interface PriceFetcherService {

    /**
     * Invoke external PriceFetcher request with provided details
     *
     * @param priceFetcherRequest
     */
    void fetchPrices(PriceFetcherRequest priceFetcherRequest, ItemPricesProcessor itemPricesProcessor);

}
