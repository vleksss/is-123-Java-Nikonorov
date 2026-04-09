package com.auction.facade;

import com.auction.dto.AuctionCardDto;
import com.auction.dto.AuctionRequest;
import com.auction.dto.BidRequest;
import com.auction.model.Auction;
import com.auction.pattern.factory.AuctionCardFactory;
import com.auction.service.AuctionService;
import com.auction.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionFacadeImpl implements AuctionFacade {
    private final AuctionService auctionService;
    private final BidService bidService;
    private final AuctionCardFactory auctionCardFactory;

    @Override
    public List<AuctionCardDto> getAuctionCards() {
        return auctionService.getAll().stream()
                .map(auction -> auctionCardFactory.create(auction, bidService.getCurrentPrice(auction.getId())))
                .toList();
    }

    @Override
    public AuctionCardDto createAuction(AuctionRequest request, String username) {
        Auction auction = auctionService.create(request, username);
        return auctionCardFactory.create(auction, bidService.getCurrentPrice(auction.getId()));
    }

    @Override
    public AuctionCardDto placeBid(BidRequest request, String username) {
        bidService.placeBid(request, username);
        Auction auction = auctionService.getById(request.getAuctionId());
        return auctionCardFactory.create(auction, bidService.getCurrentPrice(auction.getId()));
    }

    @Override
    public AuctionCardDto closeAuction(Long auctionId) {
        Auction auction = auctionService.close(auctionId);
        return auctionCardFactory.create(auction, bidService.getCurrentPrice(auction.getId()));
    }
}
