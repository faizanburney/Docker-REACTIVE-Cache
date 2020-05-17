package com.trivago.searchcore.casestudy.deals.models;

import java.util.Set;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

/**
 * This wrapper object contains a list of deals for an item and it's corresponding cache status. It is primarily used
 * for storage in DealsCache.
 */
@Value
@Builder
public class ItemDeals {

    // These static constants should be marked as final
    public static String EMPTY = "EMPTY";
    public static String FILLING = "FILLING";
    public static String DONE = "DONE";


    int itemId;

    @Singular
    Set<Deal> deals;

    // In future may be provided by Price Fetcher
    String status;

}
