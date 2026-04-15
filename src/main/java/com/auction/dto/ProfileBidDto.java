package com.auction.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class ProfileBidDto {
    private Long id;
    private Long auctionId;
    private String auctionTitle;
    private String bidderUsername;
    private BigDecimal amount;
    private LocalDateTime bidTime;
    private String auctionStatus;
}
