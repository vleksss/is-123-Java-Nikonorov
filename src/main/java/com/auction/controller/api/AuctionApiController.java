package com.auction.controller.api;

import com.auction.dto.AuctionCardDto;
import com.auction.dto.AuctionRequest;
import com.auction.facade.AuctionFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuctionApiController {
    private final AuctionFacade auctionFacade;

    @GetMapping("/auctions")
    public List<AuctionCardDto> getAll() {
        return auctionFacade.getAuctionCards();
    }

    @PostMapping("/owner/auctions")
    public AuctionCardDto create(@Valid @RequestBody AuctionRequest request, Authentication authentication) {
        return auctionFacade.createAuction(request, authentication.getName());
    }

    @PostMapping("/admin/auctions/{id}/close")
    public AuctionCardDto closeByAdmin(@PathVariable Long id) {
        return auctionFacade.closeAuctionByAdmin(id);
    }

    @PostMapping("/owner/auctions/{id}/close")
    public AuctionCardDto closeByOwner(@PathVariable Long id, Authentication authentication) {
        return auctionFacade.closeAuctionByOwner(id, authentication.getName());
    }
}
