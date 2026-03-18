package com.auction.controller;

import com.auction.model.Bid;
import com.auction.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
public class BidController {
    private final BidService bidService;

    @PostMapping public Bid placeBid(@RequestBody Bid bid){return bidService.placeBid(bid);}
}