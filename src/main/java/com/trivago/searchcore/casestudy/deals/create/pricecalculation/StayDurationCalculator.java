package com.trivago.searchcore.casestudy.deals.create.pricecalculation;

import com.trivago.triava.tcache.Cache;
import com.trivago.triava.tcache.EvictionPolicy;
import com.trivago.triava.tcache.TCacheFactory;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.cache.integration.CacheLoaderException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StayDurationCalculator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    // for every date in the next year, this saves the dates for approximately four weeks
    // (365 * 28 = 10220)
    private static final int MAX_CACHED_ELEMENTS = 10_2200;

    // we assume, that popular dates will be used more often
    // we could create an advanced eviction which removes the "oldest" dates first
    // but for now, we start simple
    private static final EvictionPolicy EVICTION_POLICY = EvictionPolicy.LFU;

    private static Cache<DateWrapper, Integer> durationCache = TCacheFactory.standardFactory() //
        .<DateWrapper, Integer>builder() //
        .setLoader(new DateCalculator()) //
        .setReadThrough(true) //
        .setMaxElements(MAX_CACHED_ELEMENTS) //
        .setEvictionPolicy(EVICTION_POLICY) //
        .build();

    public int calculateNumberOfNights(int arrivalDay, int departureDay) {
        if (arrivalDay == departureDay) {
            return 0;
        }
        if (arrivalDay > departureDay) {
            throw new IllegalArgumentException("Arrival day must be before departure day.");
        }
        final DateWrapper key = new DateWrapper(arrivalDay, departureDay);
        Integer integer = durationCache.get(key);
        if (integer == null) {
            // This can happen if the same key is requested in quick succession.
            // The calculation is still in progress and the second request gets "null" as a result.
            // It seems like the CacheLoader mechanism does not block the subsequent requests for which a cache loading
            //  is in progress.
            log.debug("Null response from cache for key {}.", key);
            integer = new DateCalculator().load(key);
        }
        return integer;
    }

    private static class DateCalculator extends com.trivago.triava.tcache.core.CacheLoader<DateWrapper, Integer> {

        @Override
        public Integer load(DateWrapper key) throws CacheLoaderException {
            LocalDate arrivalDate = LocalDate.parse(String.valueOf(key.getArrivalDay()), DATE_FORMATTER);
            LocalDate departureDate = LocalDate.parse(String.valueOf(key.getDepartureDay()), DATE_FORMATTER);

            return (int) arrivalDate.until(departureDate, ChronoUnit.DAYS);
        }
    }

    private static class DateWrapper {
        private final int arrivalDay;
        private final int departureDay;

        public DateWrapper(int arrivalDay, int departureDay) {
            this.arrivalDay = arrivalDay;
            this.departureDay = departureDay;
        }

        public int getArrivalDay() {
            return arrivalDay;
        }

        public int getDepartureDay() {
            return departureDay;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }

            DateWrapper that = (DateWrapper) other;

            if (arrivalDay != that.arrivalDay) {
                return false;
            }
            return departureDay == that.departureDay;
        }

        @Override
        public int hashCode() {
            return 31 * arrivalDay * departureDay;
        }

        @Override
        public String toString() {
            return "DateWrapper{" + "arrivalDay=" + arrivalDay + ", departureDay=" + departureDay + '}';
        }
    }
}
