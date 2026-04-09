package com.auction.service.impl;

import com.auction.dto.BidRequest;
import com.auction.model.Auction;
import com.auction.model.Bid;
import com.auction.model.User;
import com.auction.pattern.strategy.BidValidationStrategy;
import com.auction.repository.BidRepository;
import com.auction.service.AuctionService;
import com.auction.service.BidService;
import com.auction.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {
    private final BidRepository bidRepository;
    private final AuctionService auctionService;
    private final UserService userService;
    private final BidValidationStrategy bidValidationStrategy;

    @Override
    public Bid placeBid(BidRequest request, String username) {
        Auction auction = auctionService.getById(request.getAuctionId());
        User user = userService.getByUsername(username);
        BigDecimal currentPrice = getCurrentPrice(auction.getId());
        bidValidationStrategy.validate(auction, currentPrice, request.getAmount(), user.getUsername());
        Bid bid = Bid.builder()
                .amount(request.getAmount())
                .bidTime(LocalDateTime.now())
                .auction(auction)
                .user(user)
                .build();
        return bidRepository.save(bid);
    }

    @Override
    public BigDecimal getCurrentPrice(Long auctionId) {
        Auction auction = auctionService.getById(auctionId);
        return bidRepository.findTopByAuctionOrderByAmountDesc(auction)
                .map(Bid::getAmount)
                .orElse(auction.getStartPrice());
    }

    @Override
    public List<Bid> getAuctionBids(Long auctionId) {
        return bidRepository.findByAuctionOrderByAmountDesc(auctionService.getById(auctionId));
    }
}
