package com.auction.pattern.strategy;

import com.auction.model.Auction;
import com.auction.model.AuctionStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DefaultBidValidationStrategy implements BidValidationStrategy {
    @Override
    public void validate(Auction auction, BigDecimal currentPrice, BigDecimal newAmount, String bidderUsername) {
        if (auction.getStatus() != AuctionStatus.ACTIVE) {
            throw new IllegalStateException("Аукцион не активен");
        }
        if (auction.getEndTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Время аукциона истекло");
        }
        if (auction.getOwner().getUsername().equals(bidderUsername)) {
            throw new IllegalStateException("Владелец не может делать ставки на свой аукцион");
        }
        if (newAmount.compareTo(currentPrice) <= 0) {
            throw new IllegalStateException("Ставка должна быть выше текущей цены");
        }
    }
}
