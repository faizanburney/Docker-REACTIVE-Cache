package com.trivago.searchcore.casestudy.deals.models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Deal {

    int partnerId;

    // price per night in requested currency
    int pricePerNight;

    // price per stay in requested currency
    double pricePerStay;

    // The price/night in Eurocents, required for click out link generation.
    double eurocentPricePerNight;

}
