package com.trivago.searchcore.casestudy.deals.models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SingleItemDealsRequest {

    int itemId;
    DealParameters dealParameters;
}
