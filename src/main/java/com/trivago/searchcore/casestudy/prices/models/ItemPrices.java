package com.trivago.searchcore.casestudy.prices.models;

import java.util.Collections;
import java.util.Set;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class ItemPrices {

    int itemId;
    @Singular
    Set<Price> prices;

    public static ItemPrices emptyItemPrices(int itemId) {
        return ItemPrices.builder() //
            .itemId(itemId) //
            .prices(Collections.emptyList()) //
            .build();
    }
}
