package com.auction.repository;

import com.auction.model.Auction;
import com.auction.model.Bid;
import com.auction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {
    Optional<Bid> findTopByAuctionOrderByAmountDesc(Auction auction);
    List<Bid> findByAuctionOrderByAmountDesc(Auction auction);
    List<Bid> findByUserOrderByBidTimeDesc(User user);
}
