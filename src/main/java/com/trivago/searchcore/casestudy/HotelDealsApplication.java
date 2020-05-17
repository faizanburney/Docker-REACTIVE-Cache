package com.trivago.searchcore.casestudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HotelDealsApplication {

    // no point of marking this as throws exception as this
    // is entry point of code.
    public static void main(String[] args) throws Exception {
        SpringApplication.run(HotelDealsApplication.class, args);
    }

}
