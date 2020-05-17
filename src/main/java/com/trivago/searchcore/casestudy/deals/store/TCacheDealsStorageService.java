package com.trivago.searchcore.casestudy.deals.store;

import com.trivago.searchcore.casestudy.deals.models.DealsRequest;
import com.trivago.searchcore.casestudy.deals.models.ItemDeals;
import com.trivago.searchcore.casestudy.deals.models.SingleItemDealsRequest;
import com.trivago.triava.tcache.Cache;
import com.trivago.triava.tcache.TCacheFactory;
import com.trivago.triava.tcache.core.Builder;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.trivago.searchcore.casestudy.deals.models.ItemDeals.EMPTY;

/**
 * A TCache based storage which stores {@link SingleItemDealsRequest} as key and {@link ItemDeals} as the value.
 */
@Service
public class TCacheDealsStorageService implements DealsStorageService {

    private static Cache<SingleItemDealsRequest, ItemDeals> cache;

    static {
        Builder<SingleItemDealsRequest, ItemDeals> builder = TCacheFactory.standardFactory().builder();
        cache = builder.setMaxCacheTime(10, TimeUnit.MINUTES).build();
    }

    @Override
    public ItemDeals getForSingleItem(SingleItemDealsRequest singleItemDealsRequest) {

        System.out.println("GET: " + singleItemDealsRequest);

        final ItemDeals itemDeals = cache.get(singleItemDealsRequest);
        if (itemDeals == null) {
            return ItemDeals.builder()
                .itemId(singleItemDealsRequest.getItemId())
                .deals(Collections.emptyList())
                .status(EMPTY)
                .build();

        } else return itemDeals;
    }


    @Override
    public Collection<ItemDeals> getForMultipleItems(DealsRequest dealsRequest) {
        return dealsRequest.getItemIds()
            .stream()
            .map(itemId -> SingleItemDealsRequest.builder()
                .itemId(itemId)
                .dealParameters(dealsRequest.getDealParameters())
                .build())
            .map(this::getForSingleItem)
            .collect(Collectors.toList());
    }

    @Override
    public void putForSingleItem(SingleItemDealsRequest singleItemDealsRequest, ItemDeals itemDeals) {
        System.out.println("PUT: " + singleItemDealsRequest + " -- " + itemDeals);
        cache.put(singleItemDealsRequest, itemDeals);
    }
}
