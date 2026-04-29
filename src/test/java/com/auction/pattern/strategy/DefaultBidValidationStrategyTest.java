package com.auction.pattern.strategy;

import com.auction.model.Auction;
import com.auction.model.AuctionStatus;
import com.auction.model.Role;
import com.auction.model.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultBidValidationStrategyTest {
    private final DefaultBidValidationStrategy strategy = new DefaultBidValidationStrategy();

    @Test
    void validateShouldRejectInactiveAuction() {
        Auction auction = buildAuction(AuctionStatus.CLOSED, "owner", LocalDateTime.now().plusDays(1));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> strategy.validate(auction, new BigDecimal("100.00"), new BigDecimal("150.00"), "user"));

        assertEquals("Аукцион не активен", exception.getMessage());
    }

    @Test
    void validateShouldRejectOwnerBid() {
        Auction auction = buildAuction(AuctionStatus.ACTIVE, "owner", LocalDateTime.now().plusDays(1));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> strategy.validate(auction, new BigDecimal("100.00"), new BigDecimal("150.00"), "owner"));

        assertEquals("Владелец не может делать ставки на свой аукцион", exception.getMessage());
    }

    @Test
    void validateShouldRejectLowBid() {
        Auction auction = buildAuction(AuctionStatus.ACTIVE, "owner", LocalDateTime.now().plusDays(1));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> strategy.validate(auction, new BigDecimal("100.00"), new BigDecimal("100.00"), "user"));

        assertEquals("Ставка должна быть выше текущей цены", exception.getMessage());
    }

    @Test
    void validateShouldAllowCorrectBid() {
        Auction auction = buildAuction(AuctionStatus.ACTIVE, "owner", LocalDateTime.now().plusDays(1));

        assertDoesNotThrow(() ->
                strategy.validate(auction, new BigDecimal("100.00"), new BigDecimal("150.00"), "user"));
    }

    private Auction buildAuction(AuctionStatus status, String ownerUsername, LocalDateTime endTime) {
        User owner = User.builder()
                .id(1L)
                .username(ownerUsername)
                .role(Role.OWNER)
                .enabled(true)
                .build();

        return Auction.builder()
                .id(1L)
                .title("Товар")
                .description("Описание")
                .startPrice(new BigDecimal("100.00"))
                .startTime(LocalDateTime.now().minusDays(1))
                .endTime(endTime)
                .status(status)
                .owner(owner)
                .build();
    }
}