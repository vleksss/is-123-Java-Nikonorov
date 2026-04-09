package com.auction.pattern.factory;

import com.auction.dto.AuctionCardDto;
import com.auction.model.Auction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AuctionCardFactory {
    public AuctionCardDto create(Auction auction, BigDecimal currentPrice) {
        return AuctionCardDto.builder()
                .id(auction.getId())
                .title(auction.getTitle())
                .description(auction.getDescription())
                .startPrice(auction.getStartPrice())
                .currentPrice(currentPrice)
                .ownerUsername(auction.getOwner().getUsername())
                .status(auction.getStatus().name())
                .startTime(auction.getStartTime())
                .endTime(auction.getEndTime())
                .build();
    }
}
