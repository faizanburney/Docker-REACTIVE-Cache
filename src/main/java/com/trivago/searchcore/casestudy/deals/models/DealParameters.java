package com.trivago.searchcore.casestudy.deals.models;

import java.util.List;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

/**
 * Search parameters for finding deals for given Item
 */
@Value
@Builder
public class DealParameters {

    /**
     * The code that identifies the trivago hotel search platform.
     *
     * Syntax (PCRE): `/^[a-z][a-z\d]{0,23}$/D`
     */
    String platformCode;

    /**
     * The ISO 4217 currency code to get the deals in.
     *
     * Syntax (PCRE): `/^[A-Z]{3}$/D` See: https://en.wikipedia.org/wiki/ISO_4217
     */
    String currencyCode;

    /**
     * The ISO 8601 short-format date the user wants to check-in.
     *
     * Format: `YYYYMMDD` Syntax (PCRE): `/^\d{8}$/D` See: https://en.wikipedia.org/wiki/ISO_8601
     */
    int checkInDate;

    /**
     * The ISO 8601 short-format date the user wants to check-out.
     *
     * Format: `YYYYMMDD` Syntax (PCRE): `/^\d{8}$/D` See: https://en.wikipedia.org/wiki/ISO_8601
     */
    int checkOutDate;

    @Singular
    List<Room> rooms;

    /**
     * The IETF BCP47 language tag to get the room descriptions in.
     * This a pass through parameter without any affect within deal service, hence, not part of DealCacheKey
     */
    String languageTag;

}
