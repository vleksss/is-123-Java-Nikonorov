package com.auction.pattern.strategy;

import com.auction.model.Auction;

import java.math.BigDecimal;

public interface BidValidationStrategy {
    void validate(Auction auction, BigDecimal currentPrice, BigDecimal newAmount, String bidderUsername);
}
