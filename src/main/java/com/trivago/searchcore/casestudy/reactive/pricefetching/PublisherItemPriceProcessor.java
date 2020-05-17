package com.trivago.searchcore.casestudy.reactive.pricefetching;

import com.trivago.searchcore.casestudy.deals.models.SingleItemDealsRequest;
import com.trivago.searchcore.casestudy.prices.fetcher.ItemPricesProcessor;
import com.trivago.searchcore.casestudy.prices.models.ItemPrices;
import com.trivago.searchcore.casestudy.prices.models.PriceFetcherRequest;
import com.trivago.searchcore.casestudy.prices.models.PriceFetcherResponse;
import com.trivago.searchcore.casestudy.reactive.models.PriceFetcherResponseWithRequest;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.BaseSubscriber;

import static com.trivago.searchcore.casestudy.prices.models.PriceFetcherResponse.STATUS_COMPLETE;

/**
 * Try to bridge the Publisher from the reactive world with the ItemPricesProcessor
 */
@Slf4j
public class PublisherItemPriceProcessor implements ItemPricesProcessor, Publisher<PriceFetcherResponseWithRequest> {


    private Subscriber<? super PriceFetcherResponseWithRequest> subscriber;


    @Override
    public void processItemPrices(SingleItemDealsRequest singleItemDealsRequest,
        PriceFetcherResponse priceFetcherResponse) {
        subscriber.onNext(PriceFetcherResponseWithRequest.builder()
            .singleItemDealsRequest(singleItemDealsRequest)
            .priceFetcherResponse(priceFetcherResponse)
            .build());
    }

    @Override
    public void onCompleted(PriceFetcherRequest priceFetcherRequest) {

        for (Integer itemId : priceFetcherRequest.getItemIds()) {

            final PriceFetcherResponse priceFetcherResponse = PriceFetcherResponse.builder() //
                .itemPrices(ItemPrices.emptyItemPrices(itemId)) //
                .status(STATUS_COMPLETE).build();

            subscriber.onNext(PriceFetcherResponseWithRequest.builder()
                .singleItemDealsRequest(SingleItemDealsRequest.builder()
                    .itemId(itemId)
                    .dealParameters(priceFetcherRequest.getDealParameters())
                    .build())
                .priceFetcherResponse(priceFetcherResponse)
                .build());
        }

        subscriber.onComplete();
        subscriber = null;
    }

    @Override
    public void onError(PriceFetcherRequest priceFetcherRequest, Throwable throwable) {

        for (Integer itemId : priceFetcherRequest.getItemIds()) {

            final PriceFetcherResponse priceFetcherResponse = PriceFetcherResponse.builder() //
                .itemPrices(ItemPrices.emptyItemPrices(itemId)) //
                .status("ERROR").build();

            subscriber.onNext(PriceFetcherResponseWithRequest.builder()
                .singleItemDealsRequest(SingleItemDealsRequest.builder()
                    .itemId(itemId)
                    .dealParameters(priceFetcherRequest.getDealParameters())
                    .build())
                .priceFetcherResponse(priceFetcherResponse)
                .build());
        }
        System.out.println("Received an onError.");
        subscriber.onComplete();
        subscriber = null;
    }

    @Override
    public void subscribe(Subscriber<? super PriceFetcherResponseWithRequest> subscriber) {
        // using generic raw types should be avoided directly
        // to get safety at compile time.
        subscriber.onSubscribe(new BaseSubscriber() {
        });

        this.subscriber = subscriber;
    }
}
