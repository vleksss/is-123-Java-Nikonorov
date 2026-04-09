package com.auction.repository;

import com.auction.model.Auction;
import com.auction.model.AuctionStatus;
import com.auction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findByStatusOrderByEndTimeAsc(AuctionStatus status);
    List<Auction> findByOwnerOrderByEndTimeDesc(User owner);
}
