package com.auction.controller;

import com.auction.dto.AuctionRequest;
import com.auction.dto.BidRequest;
import com.auction.dto.ProfileAuctionDto;
import com.auction.dto.ProfileBidDto;
import com.auction.dto.UserAdminDto;
import com.auction.facade.AuctionFacade;
import com.auction.model.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

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

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication, HttpServletRequest request) {
        String username = authentication.getName();
        List<ProfileBidDto> bidHistory = auctionFacade.getUserBidHistory(username);
        List<ProfileAuctionDto> participating = auctionFacade.getUserParticipatingAuctions(username);
        List<ProfileAuctionDto> winning = auctionFacade.getUserWinningAuctions(username);
        model.addAttribute("bidHistory", bidHistory);
        model.addAttribute("participatingAuctions", participating);
        model.addAttribute("winningAuctions", winning);
        fillCommonModel(model, authentication, request);
        return "thymeleaf/profile/index";
    }

    @GetMapping("/owner/dashboard")
    public String ownerDashboard(Model model, Authentication authentication, HttpServletRequest request) {
        String username = authentication.getName();
        List<ProfileAuctionDto> auctions = auctionFacade.getOwnerAuctions(username);
        model.addAttribute("ownerAuctions", auctions);
        model.addAttribute("auctionRequest", new AuctionRequest());
        fillCommonModel(model, authentication, request);
        return "thymeleaf/owner/dashboard";
    }

    @GetMapping("/owner/auctions/{id}/bids")
    public String ownerAuctionBids(@PathVariable Long id, Model model, Authentication authentication, HttpServletRequest request) {
        boolean allowed = auctionFacade.getOwnerAuctions(authentication.getName()).stream()
                .anyMatch(auction -> auction.getId().equals(id));
        if (!allowed) {
            return buildRedirect(request, null, "Просматривать можно только свои аукционы");
        }
        model.addAttribute("auctionBids", auctionFacade.getAuctionBidHistory(id));
        model.addAttribute("auctionId", id);
        fillCommonModel(model, authentication, request);
        return "thymeleaf/owner/bids";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model, Authentication authentication, HttpServletRequest request) {
        List<UserAdminDto> users = auctionFacade.getUsersForAdmin();
        List<ProfileAuctionDto> auctions = auctionFacade.getAuctionCards().stream()
                .map(auction -> ProfileAuctionDto.builder()
                        .id(auction.getId())
                        .title(auction.getTitle())
                        .status(auction.getStatus())
                        .currentPrice(auction.getCurrentPrice())
                        .endTime(auction.getEndTime())
                        .ownerUsername(auction.getOwnerUsername())
                        .build())
                .toList();
        model.addAttribute("users", users);
        model.addAttribute("allAuctions", auctions);
        model.addAttribute("roles", Role.values());
        fillCommonModel(model, authentication, request);
        return "thymeleaf/admin/dashboard";
    }

    @PostMapping("/owner/auctions")
    public String createAuction(@ModelAttribute AuctionRequest request,
                                Authentication authentication,
                                HttpServletRequest httpServletRequest) {
        try {
            auctionFacade.createAuction(request, authentication.getName());
            return buildRedirect(httpServletRequest, "Аукцион создан", null);
        } catch (Exception ex) {
            return buildRedirect(httpServletRequest, null, ex.getMessage());
        }
    }

    @PostMapping("/owner/auctions/{id}/close")
    public String closeOwnerAuction(@PathVariable Long id,
                                    Authentication authentication,
                                    HttpServletRequest request) {
        try {
            auctionFacade.closeAuctionByOwner(id, authentication.getName());
            return buildRedirect(request, "Аукцион закрыт", null);
        } catch (Exception ex) {
            return buildRedirect(request, null, ex.getMessage());
        }
    }

    @PostMapping("/admin/auctions/{id}/close")
    public String closeAdminAuction(@PathVariable Long id, HttpServletRequest request) {
        try {
            auctionFacade.closeAuctionByAdmin(id);
            return buildRedirect(request, "Аукцион закрыт администратором", null);
        } catch (Exception ex) {
            return buildRedirect(request, null, ex.getMessage());
        }
    }

    @PostMapping("/admin/users/{id}/status")
    public String changeUserStatus(@PathVariable Long id,
                                   @RequestParam boolean enabled,
                                   HttpServletRequest request) {
        try {
            auctionFacade.changeUserEnabled(id, enabled);
            return buildRedirect(request, enabled ? "Пользователь разблокирован" : "Пользователь заблокирован", null);
        } catch (Exception ex) {
            return buildRedirect(request, null, ex.getMessage());
        }
    }

    @PostMapping("/admin/users/{id}/role")
    public String changeUserRole(@PathVariable Long id,
                                 @RequestParam Role role,
                                 HttpServletRequest request) {
        try {
            auctionFacade.changeUserRole(id, role);
            return buildRedirect(request, "Роль пользователя обновлена", null);
        } catch (Exception ex) {
            return buildRedirect(request, null, ex.getMessage());
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
        fillCommonModel(model, authentication, request);
    }

    private void fillCommonModel(Model model, Authentication authentication, HttpServletRequest request) {
        model.addAttribute("canCreateAuction", hasRole(authentication, "ROLE_OWNER"));
        model.addAttribute("isAdmin", hasRole(authentication, "ROLE_ADMIN"));
        model.addAttribute("isOwner", hasRole(authentication, "ROLE_OWNER"));
        model.addAttribute("currentUser", authentication != null ? authentication.getName() : null);
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
            if (referer.contains("/owner/auctions/") && referer.contains("/bids")) {
                int start = referer.indexOf("/owner/auctions/");
                int end = referer.indexOf("?", start);
                return end > start ? referer.substring(start, end) : referer.substring(start);
            }
            if (referer.contains("/owner/dashboard")) {
                return "/owner/dashboard";
            }
            if (referer.contains("/admin/dashboard")) {
                return "/admin/dashboard";
            }
            if (referer.contains("/profile")) {
                return "/profile";
            }
        }
        return "/thymeleaf/auctions";
    }
}
