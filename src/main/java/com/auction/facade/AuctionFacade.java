package com.auction.facade;

import com.auction.dto.AuctionCardDto;
import com.auction.dto.AuctionRequest;
import com.auction.dto.BidRequest;

import java.util.List;

public interface AuctionFacade {
    List<AuctionCardDto> getAuctionCards();
    AuctionCardDto createAuction(AuctionRequest request, String username);
    AuctionCardDto placeBid(BidRequest request, String username);
    AuctionCardDto closeAuction(Long auctionId);
}
