package com.auction.service;

import com.auction.dto.AuctionRequest;
import com.auction.model.Auction;

import java.util.List;

public interface AuctionService {
    Auction create(AuctionRequest request, String username);
    Auction getById(Long id);
    List<Auction> getAll();
    List<Auction> getActive();
    List<Auction> getByOwner(String username);
    Auction close(Long id);
}
