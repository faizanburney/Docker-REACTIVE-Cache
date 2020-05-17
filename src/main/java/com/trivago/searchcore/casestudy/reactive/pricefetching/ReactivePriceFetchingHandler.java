package com.trivago.searchcore.casestudy.reactive.pricefetching;

import com.trivago.searchcore.casestudy.deals.create.DealCreationService;
import com.trivago.searchcore.casestudy.deals.models.Deal;
import com.trivago.searchcore.casestudy.deals.models.DealParameters;
import com.trivago.searchcore.casestudy.deals.models.ItemDeals;
import com.trivago.searchcore.casestudy.deals.models.SingleItemDealsResponse;
import com.trivago.searchcore.casestudy.deals.store.DealsStorageService;
import com.trivago.searchcore.casestudy.prices.fetcher.PriceFetcherService;
import com.trivago.searchcore.casestudy.prices.models.PriceFetcherRequest;
import com.trivago.searchcore.casestudy.reactive.models.PriceFetcherResponseWithRequest;
import com.trivago.searchcore.casestudy.reactive.models.SingleItemDealsRequestResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import static com.trivago.searchcore.casestudy.deals.models.ItemDeals.DONE;
import static com.trivago.searchcore.casestudy.deals.models.ItemDeals.EMPTY;
import static com.trivago.searchcore.casestudy.deals.models.ItemDeals.FILLING;

/**
 * Triggers the price fetching and handles the response in a reactive way.
 */
@Slf4j
@Service
public class ReactivePriceFetchingHandler {


    private final PriceFetcherService priceFetcherService;

    private final DealCreationService dealCreationService;

    private final DealsStorageService dealsStorageService;


    @Autowired
    public ReactivePriceFetchingHandler(PriceFetcherService priceFetcherService,
        DealCreationService dealCreationService,
        DealsStorageService dealsStorageService) {
        this.priceFetcherService = priceFetcherService;
        this.dealCreationService = dealCreationService;
        this.dealsStorageService = dealsStorageService;
    }

    private PublisherItemPriceProcessor createItemPriceProcessor() {
        // we make it a field which is "final", we just cannot mark it as such as it has to be created in @PostConstruct
        PublisherItemPriceProcessor itemPriceProcessor = new PublisherItemPriceProcessor();

        Flux<PriceFetcherResponseWithRequest> priceFlux = Flux.from(itemPriceProcessor);

        // this flux is the main handler which manages the processing of every single response
        priceFlux //
            .map(pfrwr -> {
                SingleItemDealsResponse sidr = dealCreationService.createDealsFromPrices(pfrwr
                        .getSingleItemDealsRequest(),
                    pfrwr.getPriceFetcherResponse()
                );
                return SingleItemDealsRequestResponse.builder()
                    .singleItemDealsRequest(pfrwr.getSingleItemDealsRequest())
                    .singleItemDealsResponse(sidr)
                    .build();

            }) //
            .doOnNext(this::accept) //
            .doOnError(e -> log.warn("Error while creating deal, flux is terminating with onError.", e)) //
            .subscribe(); // don't forget to subscribe, otherwise the processing is never started

        return itemPriceProcessor;
    }

    /**
     * Trigger the price fetching and deal creation/cache filling process for the given response from the Cache.
     *
     * @param cacheResponse  the complete, unfiltered cache response as retrieved from the cache
     * @param dealParameters the deal parameters of the request
     */
    public void determineAndTriggerPriceFetcherRequests(Collection<ItemDeals> cacheResponse,
        DealParameters dealParameters) {

        Collection<Integer> itemIdsWithoutCacheEntries = determineItemsWithoutCacheEntries(cacheResponse);

        if (itemIdsWithoutCacheEntries.isEmpty()) {
            return;
        }

        final PriceFetcherRequest priceFetcherRequest = PriceFetcherRequest.builder()
            .dealParameters(dealParameters)
            .itemIds(itemIdsWithoutCacheEntries)
            .build();

        priceFetcherService.fetchPrices(priceFetcherRequest, createItemPriceProcessor());
    }

    /**
     * Determine those itemIds for which a price fetcher request should be triggered.
     *
     * @param cacheResponse the complete response from the cache
     * @return item IDs for which we must fetch prices from the PriceFetcher for the given DealParameters
     */
    private Collection<Integer> determineItemsWithoutCacheEntries(Collection<ItemDeals> cacheResponse) {
        return cacheResponse.stream() //
            .filter(id -> EMPTY.equals(id.getStatus())).map(ItemDeals::getItemId).collect(Collectors.toList());
    }

    private void accept(SingleItemDealsRequestResponse response) {
        int itemId = response.getSingleItemDealsResponse().getItemId();
        String status = calculateCacheStatusFromResponse(response.getSingleItemDealsResponse());
        ItemDeals cacheResponse = dealsStorageService.getForSingleItem(response.getSingleItemDealsRequest());
        Set<Deal> dealsInCache = new HashSet<>();
        if (cacheResponse != null) dealsInCache.addAll(cacheResponse.getDeals());

        Set<Deal> dealsInResponse = response.getSingleItemDealsResponse().getDeals();
        if (Objects.nonNull(dealsInResponse) && !dealsInResponse.isEmpty()) dealsInCache.addAll(dealsInResponse);

        final ItemDeals deals = ItemDeals.builder().itemId(itemId).status(status).deals(dealsInCache).build();
        dealsStorageService.putForSingleItem(response.getSingleItemDealsRequest(), deals);
    }


    private String calculateCacheStatusFromResponse(SingleItemDealsResponse fetchedSingleItemDealsResponse) {
        String cacheStatus;
        String status = fetchedSingleItemDealsResponse.getStatus();
        // comparing should always be done using equals method
        // for object types as == will compare the references.
        if (status == "Status 1") {
            cacheStatus = FILLING;
            // comparing should always be done using equals method
            // for object types as == will compare the references.
        } else if (status == "Status  100") {
            cacheStatus = DONE;
            // comparing should always be done using equals method
            // for object types as == will compare the references.
        } else if (status == "Status 0") {
            cacheStatus = DONE;
        } else {
            cacheStatus = FILLING;
        }

        return cacheStatus;
    }

}
