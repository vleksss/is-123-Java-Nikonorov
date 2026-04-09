package com.auction.controller;

import com.auction.dto.AuctionRequest;
import com.auction.dto.BidRequest;
import com.auction.facade.AuctionFacade;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequiredArgsConstructor
public class ThymeleafViewController {
    private final AuctionFacade auctionFacade;

    @GetMapping("/")
    public String root() {
        return "redirect:/thymeleaf/auctions";
    }

    @GetMapping("/thymeleaf/auctions")
    public String thymeleafAuctions(Model model, Authentication authentication, HttpServletRequest request) {
        fillAuctionPage(model, authentication, request);
        return "thymeleaf/auctions";
    }

    @GetMapping("/mustache/auctions")
    public String mustacheAuctions(Model model, Authentication authentication, HttpServletRequest request) {
        fillAuctionPage(model, authentication, request);
        model.addAttribute("title", "Аукцион на Mustache");
        return "mustache/auctions";
    }

    @GetMapping("/freemarker/auctions")
    public String freemarkerAuctions(Model model, Authentication authentication, HttpServletRequest request) {
        fillAuctionPage(model, authentication, request);
        model.addAttribute("pageTitle", "Аукцион на FreeMarker");
        return "freemarker/auctions";
    }

    @PostMapping("/owner/auctions")
    public String createAuction(@ModelAttribute AuctionRequest request,
                                Authentication authentication,
                                HttpServletRequest httpServletRequest) {
        try {
            auctionFacade.createAuction(request, authentication.getName());
            return buildRedirect(httpServletRequest, "Объявление создано", null);
        } catch (Exception ex) {
            return buildRedirect(httpServletRequest, null, ex.getMessage());
        }
    }

    @PostMapping("/bids")
    public String placeBid(@ModelAttribute BidRequest request,
                           Authentication authentication,
                           HttpServletRequest httpServletRequest) {
        try {
            auctionFacade.placeBid(request, authentication.getName());
            return buildRedirect(httpServletRequest, "Ставка принята", null);
        } catch (Exception ex) {
            return buildRedirect(httpServletRequest, null, ex.getMessage());
        }
    }

    private void fillAuctionPage(Model model, Authentication authentication, HttpServletRequest request) {
        model.addAttribute("auctions", auctionFacade.getAuctionCards());
        model.addAttribute("canCreateAuction", hasRole(authentication, "ROLE_OWNER"));
        model.addAttribute("errorMessage", request.getParameter("error"));
        model.addAttribute("successMessage", request.getParameter("success"));
    }

    private boolean hasRole(Authentication authentication, String role) {
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role::equals);
    }

    private String buildRedirect(HttpServletRequest request, String successMessage, String errorMessage) {
        String path = resolveBaseRedirect(request);
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(path);
        if (successMessage != null && !successMessage.isBlank()) {
            builder.queryParam("success", successMessage);
        }
        if (errorMessage != null && !errorMessage.isBlank()) {
            builder.queryParam("error", errorMessage);
        }
        return "redirect:" + builder.build().encode().toUriString();
    }

    private String resolveBaseRedirect(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer != null) {
            if (referer.contains("/mustache/auctions")) {
                return "/mustache/auctions";
            }
            if (referer.contains("/freemarker/auctions")) {
                return "/freemarker/auctions";
            }
        }
        return "/thymeleaf/auctions";
    }
}