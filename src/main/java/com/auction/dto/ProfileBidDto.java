package com.auction.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class ProfileBidDto {
    private static final DateTimeFormatter VIEW_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private Long id;
    private Long auctionId;
    private String auctionTitle;
    private String bidderUsername;
    private BigDecimal amount;
    private LocalDateTime bidTime;
    private String auctionStatus;

    public String getFormattedBidTime() {
        return bidTime == null ? "" : bidTime.format(VIEW_FORMATTER);
    }
}
