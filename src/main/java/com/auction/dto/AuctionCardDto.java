package com.auction.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class AuctionCardDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal startPrice;
    private BigDecimal currentPrice;
    private String ownerUsername;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
