package com.auction.controller;

import com.auction.model.Auction;
import com.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {
    private final AuctionService auctionService;

    @PostMapping public Auction createAuction(@RequestBody Auction auction){return auctionService.createAuction(auction);}
    @GetMapping("/{id}") public Auction getAuction(@PathVariable Long id){return auctionService.getAuction(id);}
    @GetMapping public List<Auction> getAllAuctions(){return auctionService.getAllAuctions();}
}