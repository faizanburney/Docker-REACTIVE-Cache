package com.trivago.searchcore.casestudy.reactive.models;

import com.trivago.searchcore.casestudy.deals.models.SingleItemDealsRequest;
import com.trivago.searchcore.casestudy.deals.models.SingleItemDealsResponse;

import lombok.Builder;
import lombok.Value;

/**
 * Group a response for a single item with its request. Needed in the Flux. 
 */
@Value
@Builder
public class SingleItemDealsRequestResponse {

    SingleItemDealsRequest singleItemDealsRequest;
    SingleItemDealsResponse singleItemDealsResponse;
    
}
