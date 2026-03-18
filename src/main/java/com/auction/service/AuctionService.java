package com.auction.service;

import com.auction.model.Auction;
import java.util.List;

public interface AuctionService {
    Auction createAuction(Auction auction);
    Auction getAuction(Long id);
    List<Auction> getAllAuctions();
}