package com.auction.service.impl;

import com.auction.dto.BidRequest;
import com.auction.model.Auction;
import com.auction.model.AuctionStatus;
import com.auction.model.Bid;
import com.auction.model.Role;
import com.auction.model.User;
import com.auction.pattern.strategy.BidValidationStrategy;
import com.auction.repository.BidRepository;
import com.auction.service.AuctionService;
import com.auction.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BidServiceImplTest {
    @Mock
    private BidRepository bidRepository;

    @Mock
    private AuctionService auctionService;

    @Mock
    private UserService userService;

    @Mock
    private BidValidationStrategy bidValidationStrategy;

    @InjectMocks
    private BidServiceImpl bidService;

    @Test
    void placeBidShouldSaveBidForEnabledUser() {
        User owner = User.builder()
                .id(10L)
                .username("owner")
                .role(Role.OWNER)
                .enabled(true)
                .build();

        Auction auction = Auction.builder()
                .id(1L)
                .title("Лот")
                .startPrice(new BigDecimal("100.00"))
                .status(AuctionStatus.ACTIVE)
                .endTime(LocalDateTime.now().plusDays(1))
                .owner(owner)
                .build();

        User bidder = User.builder()
                .id(20L)
                .username("user")
                .role(Role.USER)
                .enabled(true)
                .build();

        BidRequest request = new BidRequest();
        request.setAuctionId(1L);
        request.setAmount(new BigDecimal("150.00"));

        when(auctionService.getById(1L)).thenReturn(auction);
        when(userService.getByUsername("user")).thenReturn(bidder);
        when(bidRepository.findTopByAuctionOrderByAmountDesc(auction)).thenReturn(Optional.empty());
        when(bidRepository.save(any(Bid.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Bid bid = bidService.placeBid(request, "user");

        assertEquals(new BigDecimal("150.00"), bid.getAmount());
        assertEquals(bidder, bid.getUser());
        assertEquals(auction, bid.getAuction());
        verify(bidValidationStrategy).validate(auction, new BigDecimal("100.00"), new BigDecimal("150.00"), "user");
    }

    @Test
    void placeBidShouldRejectDisabledUser() {
        User owner = User.builder()
                .id(10L)
                .username("owner")
                .role(Role.OWNER)
                .enabled(true)
                .build();

        Auction auction = Auction.builder()
                .id(1L)
                .title("Лот")
                .startPrice(new BigDecimal("100.00"))
                .status(AuctionStatus.ACTIVE)
                .endTime(LocalDateTime.now().plusDays(1))
                .owner(owner)
                .build();

        User blockedUser = User.builder()
                .id(20L)
                .username("blocked")
                .role(Role.USER)
                .enabled(false)
                .build();

        BidRequest request = new BidRequest();
        request.setAuctionId(1L);
        request.setAmount(new BigDecimal("150.00"));

        when(auctionService.getById(1L)).thenReturn(auction);
        when(userService.getByUsername("blocked")).thenReturn(blockedUser);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> bidService.placeBid(request, "blocked"));
        assertEquals("Ваш аккаунт заблокирован", exception.getMessage());
    }
}