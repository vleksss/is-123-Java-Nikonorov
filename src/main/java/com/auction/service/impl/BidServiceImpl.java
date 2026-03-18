package com.auction.service.impl;

import com.auction.model.Bid;
import com.auction.repository.BidRepository;
import com.auction.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {
    private final BidRepository bidRepository;

    @Override
    public Bid placeBid(Bid bid) {
        bid.setTime(LocalDateTime.now());
        return bidRepository.save(bid);
    }
}