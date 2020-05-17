package com.trivago.searchcore.casestudy.prices.models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PriceFetcherResponse {
    public static final String STATUS_IN_PROGRESS = "Status 1";
    public static final String STATUS_EROR = "Status 100";
    public static final String STATUS_COMPLETE = "Status 0";
    ItemPrices itemPrices;
    String status;

}
