package com.trivago.searchcore.casestudy.deals.store;

import com.trivago.searchcore.casestudy.deals.models.DealsRequest;
import com.trivago.searchcore.casestudy.deals.models.ItemDeals;
import com.trivago.searchcore.casestudy.deals.models.SingleItemDealsRequest;

import java.util.Collection;
//unused import
import java.util.List;
//unused import
import java.util.Map;

public interface DealsStorageService {

    /**
     * Gets deals from underlying storage
     *
     * @param singleItemDealsRequest
     * @return
     */
    ItemDeals getForSingleItem(SingleItemDealsRequest singleItemDealsRequest);

    /**
     * Get deals for multiple items at once from underlying storage.
     *
     * @param dealsRequest
     * @return
     */
    Collection<ItemDeals> getForMultipleItems(DealsRequest dealsRequest);

    /**
     * Replaces or puts SingleItemDealsResponse to given singleItemDealsRequest
     *
     * @param singleItemDealsRequest
     * @param itemDeals
     */
    void putForSingleItem(SingleItemDealsRequest singleItemDealsRequest, ItemDeals itemDeals);

}
