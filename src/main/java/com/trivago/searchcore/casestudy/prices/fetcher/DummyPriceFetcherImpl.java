package com.trivago.searchcore.casestudy.prices.fetcher;

import com.trivago.searchcore.casestudy.deals.models.SingleItemDealsRequest;
import com.trivago.searchcore.casestudy.prices.models.ItemPrices;
import com.trivago.searchcore.casestudy.prices.models.Price;
import com.trivago.searchcore.casestudy.prices.models.PriceFetcherRequest;
import com.trivago.searchcore.casestudy.prices.models.PriceFetcherResponse;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static com.trivago.searchcore.casestudy.prices.models.PriceFetcherResponse.STATUS_COMPLETE;
import static com.trivago.searchcore.casestudy.prices.models.PriceFetcherResponse.STATUS_IN_PROGRESS;

@Component
public class DummyPriceFetcherImpl implements PriceFetcherService {


    @Override
    public void fetchPrices(PriceFetcherRequest priceFetcherRequest, ItemPricesProcessor itemPricesProcessor) {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int k = 0; k < 1000; k++) {
            if (k >= priceFetcherRequest.getItemIds().size()) {
                break;
            }
            int item_id = priceFetcherRequest.getItemIds().get(k);

            List<Price> prices = Arrays.asList(getRandomPrice(random), getRandomPrice(random));
            ItemPrices itemPrices = ItemPrices.builder().itemId(item_id).prices(prices).build();
            itemPricesProcessor.processItemPrices(SingleItemDealsRequest.builder()
                .itemId(item_id)
                .dealParameters(priceFetcherRequest.getDealParameters())
                .build(), PriceFetcherResponse.builder().status(STATUS_IN_PROGRESS).itemPrices(itemPrices).build());
        }


        for (int i = 0; i < 1000; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(100l);
            } catch (InterruptedException e) {
                itemPricesProcessor.onError(priceFetcherRequest, e);
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            if (i >= priceFetcherRequest.getItemIds().size()) {
                break;
            }
            int item_id = priceFetcherRequest.getItemIds().get(i);
            List<Price> prices = new ArrayList<>();
            for (int j = 0; j < random.nextInt(15); j ++) {
                prices.add(getRandomPrice(random));
            }
            ItemPrices itemPrices = ItemPrices.builder().itemId(item_id).prices(prices).build();
            itemPricesProcessor.processItemPrices(SingleItemDealsRequest.builder()
                .itemId(item_id)
                .dealParameters(priceFetcherRequest.getDealParameters())
                .build(), PriceFetcherResponse.builder().status(STATUS_COMPLETE).itemPrices(itemPrices).build());
        }
        itemPricesProcessor.onCompleted(priceFetcherRequest);
    }

    private Price getRandomPrice(ThreadLocalRandom random) {
        return Price.builder()
            .currencyCode("EUR")
            .partnerId(random.nextInt(0,100))
            .calculatedPrice(random.nextDouble(500,25000))
            .build();
    }
}
