package com.trivago.searchcore.casestudy.reactive.models;

import com.trivago.searchcore.casestudy.deals.models.SingleItemDealsRequest;
import com.trivago.searchcore.casestudy.prices.models.PriceFetcherResponse;

import lombok.Builder;
import lombok.Value;

/**
 * Group a request with a response from the pricefetcher. Needed in the Flux.
 */
@Value
@Builder
public class PriceFetcherResponseWithRequest {

    SingleItemDealsRequest singleItemDealsRequest;
    PriceFetcherResponse priceFetcherResponse;

}
