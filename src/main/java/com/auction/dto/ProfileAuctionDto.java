package com.auction.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class ProfileAuctionDto {
    private Long id;
    private String title;
    private String status;
    private BigDecimal currentPrice;
    private LocalDateTime endTime;
    private String ownerUsername;
}
