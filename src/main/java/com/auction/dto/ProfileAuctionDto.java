package com.auction.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class ProfileAuctionDto {
    private static final DateTimeFormatter VIEW_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private Long id;
    private String title;
    private String status;
    private BigDecimal currentPrice;
    private LocalDateTime endTime;
    private String ownerUsername;

    public String getFormattedEndTime() {
        return endTime == null ? "" : endTime.format(VIEW_FORMATTER);
    }
}
