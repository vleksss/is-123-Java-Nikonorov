package com.auction.pattern.factory;

import com.auction.dto.AuctionCardDto;
import com.auction.model.Auction;
import com.auction.model.AuctionStatus;
import com.auction.model.Role;
import com.auction.model.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuctionCardFactoryTest {
    @Test
    void createShouldBuildAuctionCardDto() {
        User owner = User.builder()
                .id(1L)
                .username("owner")
                .role(Role.OWNER)
                .enabled(true)
                .build();

        Auction auction = Auction.builder()
                .id(5L)
                .title("Смартфон")
                .description("Новый смартфон")
                .startPrice(new BigDecimal("10000.00"))
                .startTime(LocalDateTime.of(2026, 1, 1, 10, 0))
                .endTime(LocalDateTime.of(2026, 1, 10, 10, 0))
                .status(AuctionStatus.ACTIVE)
                .owner(owner)
                .build();

        AuctionCardFactory factory = new AuctionCardFactory();
        AuctionCardDto dto = factory.create(auction, new BigDecimal("12000.00"));

        assertEquals(5L, dto.getId());
        assertEquals("Смартфон", dto.getTitle());
        assertEquals("Новый смартфон", dto.getDescription());
        assertEquals(new BigDecimal("10000.00"), dto.getStartPrice());
        assertEquals(new BigDecimal("12000.00"), dto.getCurrentPrice());
        assertEquals("owner", dto.getOwnerUsername());
        assertEquals("ACTIVE", dto.getStatus());
        assertEquals(LocalDateTime.of(2026, 1, 1, 10, 0), dto.getStartTime());
        assertEquals(LocalDateTime.of(2026, 1, 10, 10, 0), dto.getEndTime());
    }
}