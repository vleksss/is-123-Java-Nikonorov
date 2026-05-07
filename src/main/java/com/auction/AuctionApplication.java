package com.auction;

import com.auction.config.DatabaseBootstrap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuctionApplication {
    public static void main(String[] args) {
        DatabaseBootstrap.initialize();
        SpringApplication.run(AuctionApplication.class, args);
    }
}