package com.trivago.searchcore.casestudy.prices.models;

import com.trivago.searchcore.casestudy.deals.models.DealParameters;

import java.util.List;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class PriceFetcherRequest {

    DealParameters dealParameters;

    @Singular
    List<Integer> itemIds;
}
