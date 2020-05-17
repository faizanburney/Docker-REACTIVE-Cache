package com.trivago.searchcore.casestudy.deals.create.pricecosmetics;

/**
 * Different currencies or locales have different definitions of how prices should be displayed. The definition of these
 * values are stored in objects of this type.
 * In our database the only configuration that is provided is a string with the name of the corresponding enum.
 *
 * Examples:
 * <ul>
 * <li>value=12334567, and roundDigits=2, padDigits=0 => result 123346</li>
 * <li>value=12334567, and roundDigits=4, padDigits=2 => result 123300</li>
 * </ul>
 */
public enum CurrencyDisplayConfiguration {
    EURO_CENTS(0, 0, RoundingMode.ROUND), //
    UNITS(2, 0, RoundingMode.ROUND), // 
    DOZENS_OF_UNITS(3, 1, RoundingMode.CEILING), //
    HUNDREDS_OF_UNITS(4, 2, RoundingMode.CEILING), // 
    THOUSANDS_OF_UNITS(5, 3, RoundingMode.CEILING), //
    TEN_THOUSANDS_OF_UNITS(6, 4, RoundingMode.CEILING);

    CurrencyDisplayConfiguration(int roundDigits, int padDigits, RoundingMode priceRoundingMode) {
        this.roundDigits = roundDigits;
        this.padDigits = padDigits;
        this.roundingMode = priceRoundingMode;
    }

    // number of digits which are to be rounded at the end of a number,
    // e.g. for roundDigits = 2, the price 10.98EUR stored as 1098L will be rounded to 10L
    private int roundDigits;

    // number of extra 0s which are to be appended to the price after rounding.
    // this allows us to write some prices in rounded units of 1000
    // e.g. for padDigits = 3, a price stored as 10L would become 10_000L
    private int padDigits;

    // rounding mode indicates, whether a price should be rounded up, down or to the nearest value
    private RoundingMode roundingMode;

    // not using the java.math.RoundingMode, because we do not implement its full functionality
    public enum RoundingMode {
        ROUND, FLOOR, CEILING
    }

    public int getRoundDigits() {
        return roundDigits;
    }

    public int getPadDigits() {
        return padDigits;
    }

    public RoundingMode getRoundingMode() {
        return roundingMode;
    }
}
