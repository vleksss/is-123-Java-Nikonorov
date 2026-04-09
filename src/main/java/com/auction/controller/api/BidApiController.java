package com.auction.controller.api;

import com.auction.dto.AuctionCardDto;
import com.auction.dto.BidRequest;
import com.auction.facade.AuctionFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BidApiController {
    private final AuctionFacade auctionFacade;

    @PostMapping("/bids")
    public AuctionCardDto placeBid(@Valid @RequestBody BidRequest request, Authentication authentication) {
        return auctionFacade.placeBid(request, authentication.getName());
    }
}
