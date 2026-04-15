package com.auction.facade;

import com.auction.dto.AuctionCardDto;
import com.auction.dto.AuctionRequest;
import com.auction.dto.BidRequest;
import com.auction.dto.ProfileAuctionDto;
import com.auction.dto.ProfileBidDto;
import com.auction.dto.UserAdminDto;
import com.auction.model.Role;

import java.util.List;

public interface AuctionFacade {
    List<AuctionCardDto> getAuctionCards();
    AuctionCardDto createAuction(AuctionRequest request, String username);
    AuctionCardDto placeBid(BidRequest request, String username);
    AuctionCardDto closeAuctionByAdmin(Long auctionId);
    AuctionCardDto closeAuctionByOwner(Long auctionId, String username);
    List<ProfileBidDto> getUserBidHistory(String username);
    List<ProfileAuctionDto> getUserParticipatingAuctions(String username);
    List<ProfileAuctionDto> getUserWinningAuctions(String username);
    List<ProfileAuctionDto> getOwnerAuctions(String username);
    List<ProfileBidDto> getAuctionBidHistory(Long auctionId);
    List<UserAdminDto> getUsersForAdmin();
    UserAdminDto changeUserEnabled(Long userId, boolean enabled);
    UserAdminDto changeUserRole(Long userId, Role role);
}
