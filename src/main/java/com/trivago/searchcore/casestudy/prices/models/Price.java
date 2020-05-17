package com.trivago.searchcore.casestudy.prices.models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Price {

    int partnerId;

    double calculatedPrice;

    String currencyCode;

}
