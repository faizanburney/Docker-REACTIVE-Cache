package com.trivago.searchcore.casestudy.reactive;

import com.trivago.searchcore.casestudy.deals.DealsProvider;
import com.trivago.searchcore.casestudy.deals.models.DealsRequest;
import com.trivago.searchcore.casestudy.deals.models.DealsResponse;
import com.trivago.searchcore.casestudy.deals.models.ItemDeals;
import com.trivago.searchcore.casestudy.deals.store.DealsStorageService;
import com.trivago.searchcore.casestudy.reactive.pricefetching.ReactivePriceFetchingHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import static com.trivago.searchcore.casestudy.deals.models.ItemDeals.DONE;
import static com.trivago.searchcore.casestudy.deals.models.ItemDeals.EMPTY;

/**
 * A {@link DealsProvider} which is based upon the reactive streams approach, utilizing the Reactor library.
 */
@Slf4j
@Service
public class ReactiveDealsProvider implements DealsProvider {

    private final Scheduler pricefetchingScheduler = Schedulers.newParallel("reactive-pricefetcher", 10, true);
    @Autowired
    private DealsStorageService dealsStorageService;
    @Autowired
    private ReactivePriceFetchingHandler reactivePriceFetchingHandler;

    @Override
    public DealsResponse fetchDeals(DealsRequest dealsRequest) {

        final Collection<ItemDeals> cacheResponse = dealsStorageService.getForMultipleItems(dealsRequest);

        // comparing should always be done using equals method
        // for object types as == will compare the references.
        final boolean emptyCacheResponsePresent = cacheResponse.stream()
            .anyMatch(sdr -> EMPTY == sdr.getStatus());

        if (emptyCacheResponsePresent) {
            final Mono<Mono<Object>> callableMono = Mono // this is the reactive way of executing things
                // asynchronously
                .fromCallable(() -> {
                    reactivePriceFetchingHandler.determineAndTriggerPriceFetcherRequests(cacheResponse,
                        dealsRequest.getDealParameters()
                    );
                    return Mono.empty();
                }) //
                .subscribeOn(pricefetchingScheduler);
            callableMono.subscribe();
        }


        boolean stillInProgress = cacheResponse //
            .stream() //
            // comparing should always be done using equals method
            // for object types as == will compare the references.
            .anyMatch(id -> id.getStatus() != DONE);

        return DealsResponse.builder()
            .complete(!stillInProgress)
            .itemDeals(cacheResponse)
            .build();
    }

}
