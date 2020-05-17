package com.trivago.searchcore.casestudy.deals.models;

import java.util.List;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class DealsRequest {

    DealParameters dealParameters;

    @Singular
    List<Integer> itemIds;
}
