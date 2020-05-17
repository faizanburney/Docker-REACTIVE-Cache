package com.trivago.searchcore.casestudy.deals.models;

import java.util.Set;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

/**
 * This class contains the response (from price fetcher) for one single item. The status indicates the response status
 * from price fetcher and thus whether more deals for that item are expected. {@link ItemDeals} contains also item_id,
 * deals and a status, it is used for internal caching though and the status reflects the status used for caching/cache
 * handling. This status is influenced by price fetcher response status.
 */
@Value
@Builder
public class SingleItemDealsResponse {

    int itemId;
    @Singular
    Set<Deal> deals;

    String status;
}
