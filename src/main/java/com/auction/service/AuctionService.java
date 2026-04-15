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
    List<Auction> getParticipating(String username);
    List<Auction> getWinning(String username);
    Auction closeByAdmin(Long id);
    Auction closeByOwner(Long id, String username);
}
