package com.trivago.searchcore.casestudy.deals;

import com.trivago.searchcore.casestudy.deals.models.DealsRequest;
import com.trivago.searchcore.casestudy.deals.models.DealsResponse;

/**
 * This interface provides deals for a given SingleDealsRequest.
 */
public interface DealsProvider {

    DealsResponse fetchDeals(DealsRequest dealsRequest);
}
