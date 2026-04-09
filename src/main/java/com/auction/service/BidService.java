package com.auction.service;

import com.auction.dto.BidRequest;
import com.auction.model.Bid;

import java.math.BigDecimal;
import java.util.List;

public interface BidService {
    Bid placeBid(BidRequest request, String username);
    BigDecimal getCurrentPrice(Long auctionId);
    List<Bid> getAuctionBids(Long auctionId);
}
