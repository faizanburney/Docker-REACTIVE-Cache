package com.trivago.searchcore.casestudy.web;

import com.trivago.searchcore.casestudy.deals.DealsProvider;
import com.trivago.searchcore.casestudy.deals.models.DealParameters;
import com.trivago.searchcore.casestudy.deals.models.DealsRequest;
import com.trivago.searchcore.casestudy.deals.models.DealsResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// unused import
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class WebController {

    private final List<Integer> allItemIds = Arrays.asList(
        54752,
        114312,
        1765843,
        1369926,
        136817,
        6894238,
        3814536,
        17745301,
        387296,
        1344402,
        1248613,
        3814720,
        12912,
        48016,
        34121,
        4427994,
        27282,
        957277,
        81902,
        39015,
        45894
    );
    @Autowired
    DealsProvider dealsProvider;

    // No http method is specified.This will catch all
    // request method like post,put,delete,get.That means
    // a method you intended only to be used for POST could
    // also be called by a GET allowing hackers to call the method
    // inappropriately.
    @RequestMapping("/randomprices")
    public DealsResponse generateRandomPrices() {
        DealsRequest dealsRequest = DealsRequest.builder()
            .dealParameters(DealParameters.builder()
                .checkInDate(20200101)
                .checkOutDate(20200110)
                .currencyCode("EUR")
                .platformCode("DE")
                .languageTag("DE")
                .build())
            .itemIds(allItemIds)
            .build();
        return dealsProvider.fetchDeals(dealsRequest);

    }

    // No http method is specified.This will catch all
    // request method like post,put,delete,get.That means
    // a method you intended only to be used for POST could
    // also be called by a GET allowing hackers to call the method
    // inappropriately.
    // try this request http://localhost:8080/api/prices?checkInDate=20181002&checkOutDate=20181007&currencyCode=EUR&platformCode=DE&languageTag=DE&itemIds=10,12,2310,123124,123232,1983
    @RequestMapping("/prices")
    public DealsResponse getPricesForParameters(@RequestParam int checkInDate,
        @RequestParam int checkOutDate,
        @RequestParam String currencyCode,
        @RequestParam String platformCode,
        @RequestParam String languageTag,
        @RequestParam List<Integer> itemIds){
        DealsRequest dealsRequest = DealsRequest.builder()
            .dealParameters(DealParameters.builder()
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .currencyCode(currencyCode)
                .platformCode(platformCode)
                .languageTag(languageTag)
                .build())
            .itemIds(itemIds)
            .build();
        return dealsProvider.fetchDeals(dealsRequest);

    }


}
