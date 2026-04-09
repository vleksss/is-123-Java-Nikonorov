package com.auction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BidRequest {
    @NotNull
    private Long auctionId;

    @NotNull
    @DecimalMin("1.00")
    private BigDecimal amount;
}
