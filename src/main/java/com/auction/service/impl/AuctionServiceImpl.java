package com.auction.service.impl;

import com.auction.model.Auction;
import com.auction.repository.AuctionRepository;
import com.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {
    private final AuctionRepository auctionRepository;

    @Override
    public Auction createAuction(Auction auction) { return auctionRepository.save(auction); }

    @Override
    public Auction getAuction(Long id) { return auctionRepository.findById(id).orElse(null); }

    @Override
    public List<Auction> getAllAuctions() { return auctionRepository.findAll(); }
}