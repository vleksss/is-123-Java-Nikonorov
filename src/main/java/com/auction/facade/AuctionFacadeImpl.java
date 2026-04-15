package com.auction.facade;

import com.auction.dto.AuctionCardDto;
import com.auction.dto.AuctionRequest;
import com.auction.dto.BidRequest;
import com.auction.dto.ProfileAuctionDto;
import com.auction.dto.ProfileBidDto;
import com.auction.dto.UserAdminDto;
import com.auction.model.Auction;
import com.auction.model.Bid;
import com.auction.model.Role;
import com.auction.model.User;
import com.auction.pattern.factory.AuctionCardFactory;
import com.auction.service.AuctionService;
import com.auction.service.BidService;
import com.auction.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionFacadeImpl implements AuctionFacade {
    private final AuctionService auctionService;
    private final BidService bidService;
    private final UserService userService;
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
    public AuctionCardDto closeAuctionByAdmin(Long auctionId) {
        Auction auction = auctionService.closeByAdmin(auctionId);
        return auctionCardFactory.create(auction, bidService.getCurrentPrice(auction.getId()));
    }

    @Override
    public AuctionCardDto closeAuctionByOwner(Long auctionId, String username) {
        Auction auction = auctionService.closeByOwner(auctionId, username);
        return auctionCardFactory.create(auction, bidService.getCurrentPrice(auction.getId()));
    }

    @Override
    public List<ProfileBidDto> getUserBidHistory(String username) {
        return bidService.getUserBids(username).stream()
                .map(this::toProfileBid)
                .toList();
    }

    @Override
    public List<ProfileAuctionDto> getUserParticipatingAuctions(String username) {
        return auctionService.getParticipating(username).stream()
                .map(this::toProfileAuction)
                .toList();
    }

    @Override
    public List<ProfileAuctionDto> getUserWinningAuctions(String username) {
        return auctionService.getWinning(username).stream()
                .map(this::toProfileAuction)
                .toList();
    }

    @Override
    public List<ProfileAuctionDto> getOwnerAuctions(String username) {
        return auctionService.getByOwner(username).stream()
                .map(this::toProfileAuction)
                .toList();
    }

    @Override
    public List<ProfileBidDto> getAuctionBidHistory(Long auctionId) {
        return bidService.getAuctionBids(auctionId).stream()
                .map(this::toProfileBid)
                .toList();
    }

    @Override
    public List<UserAdminDto> getUsersForAdmin() {
        return userService.getAll().stream()
                .map(this::toUserAdmin)
                .toList();
    }

    @Override
    public UserAdminDto changeUserEnabled(Long userId, boolean enabled) {
        return toUserAdmin(userService.changeEnabled(userId, enabled));
    }

    @Override
    public UserAdminDto changeUserRole(Long userId, Role role) {
        return toUserAdmin(userService.updateRole(userId, role));
    }

    private ProfileAuctionDto toProfileAuction(Auction auction) {
        return ProfileAuctionDto.builder()
                .id(auction.getId())
                .title(auction.getTitle())
                .status(auction.getStatus().name())
                .currentPrice(bidService.getCurrentPrice(auction.getId()))
                .endTime(auction.getEndTime())
                .ownerUsername(auction.getOwner().getUsername())
                .build();
    }

    private ProfileBidDto toProfileBid(Bid bid) {
        return ProfileBidDto.builder()
                .id(bid.getId())
                .auctionId(bid.getAuction().getId())
                .auctionTitle(bid.getAuction().getTitle())
                .bidderUsername(bid.getUser().getUsername())
                .amount(bid.getAmount())
                .bidTime(bid.getBidTime())
                .auctionStatus(bid.getAuction().getStatus().name())
                .build();
    }

    private UserAdminDto toUserAdmin(User user) {
        return UserAdminDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .enabled(user.isEnabled())
                .build();
    }
}
