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

    @GetMapping("/profile")
    public String profileRedirect() {
        return "redirect:/thymeleaf/profile";
    }

    @GetMapping("/owner/dashboard")
    public String ownerRedirect() {
        return "redirect:/thymeleaf/owner/dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminRedirect() {
        return "redirect:/thymeleaf/admin/dashboard";
    }

    @GetMapping("/{engine:thymeleaf|mustache|freemarker}/auctions")
    public String auctions(@PathVariable String engine, Model model, Authentication authentication, HttpServletRequest request) {
        model.addAttribute("auctions", auctionFacade.getAuctionCards());
        fillCommonModel(model, authentication, request, engine, "auctions");
        return view(engine, "auctions");
    }

    @GetMapping("/{engine:thymeleaf|mustache|freemarker}/profile")
    public String profile(@PathVariable String engine, Model model, Authentication authentication, HttpServletRequest request) {
        String username = authentication.getName();
        model.addAttribute("bidHistory", auctionFacade.getUserBidHistory(username));
        model.addAttribute("participatingAuctions", auctionFacade.getUserParticipatingAuctions(username));
        model.addAttribute("winningAuctions", auctionFacade.getUserWinningAuctions(username));
        fillCommonModel(model, authentication, request, engine, "profile");
        return view(engine, "profile");
    }

    @GetMapping("/{engine:thymeleaf|mustache|freemarker}/owner/dashboard")
    public String ownerDashboard(@PathVariable String engine, Model model, Authentication authentication, HttpServletRequest request) {
        model.addAttribute("ownerAuctions", auctionFacade.getOwnerAuctions(authentication.getName()));
        fillCommonModel(model, authentication, request, engine, "owner/dashboard");
        return view(engine, "owner-dashboard");
    }

    @GetMapping("/{engine:thymeleaf|mustache|freemarker}/owner/auctions/{id}/bids")
    public String ownerAuctionBids(@PathVariable String engine, @PathVariable Long id, Model model, Authentication authentication, HttpServletRequest request) {
        boolean allowed = auctionFacade.getOwnerAuctions(authentication.getName()).stream().anyMatch(auction -> auction.getId().equals(id));
        if (!allowed) {
            return buildRedirect(engine, "owner/dashboard", null, "Просматривать можно только свои аукционы");
        }
        model.addAttribute("auctionBids", auctionFacade.getAuctionBidHistory(id));
        model.addAttribute("auctionId", id);
        fillCommonModel(model, authentication, request, engine, "owner/auctions/" + id + "/bids");
        return view(engine, "owner-bids");
    }

    @GetMapping("/{engine:thymeleaf|mustache|freemarker}/admin/dashboard")
    public String adminDashboard(@PathVariable String engine, Model model, Authentication authentication, HttpServletRequest request) {
        List<UserAdminDto> users = auctionFacade.getUsersForAdmin(authentication.getName());
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
        fillCommonModel(model, authentication, request, engine, "admin/dashboard");
        return view(engine, "admin-dashboard");
    }

    @PostMapping("/owner/auctions")
    public String createAuction(@ModelAttribute AuctionRequest request, Authentication authentication, HttpServletRequest httpServletRequest) {
        String engine = resolveEngine(httpServletRequest, authentication, "auctions");
        try {
            auctionFacade.createAuction(request, authentication.getName());
            return buildRedirect(engine, "auctions", "Аукцион создан", null);
        } catch (Exception ex) {
            return buildRedirect(engine, "auctions", null, ex.getMessage());
        }
    }

    @PostMapping("/owner/auctions/{id}/close")
    public String closeOwnerAuctionDefault(@PathVariable Long id, Authentication authentication, HttpServletRequest request) {
        String engine = resolveEngine(request, authentication, "owner/dashboard");
        return closeOwnerAuctionInternal(engine, id, authentication);
    }

    @PostMapping("/{engine:thymeleaf|mustache|freemarker}/owner/auctions/{id}/close")
    public String closeOwnerAuction(@PathVariable String engine, @PathVariable Long id, Authentication authentication) {
        return closeOwnerAuctionInternal(engine, id, authentication);
    }

    private String closeOwnerAuctionInternal(String engine, Long id, Authentication authentication) {
        try {
            auctionFacade.closeAuctionByOwner(id, authentication.getName());
            return buildRedirect(engine, "owner/dashboard", "Аукцион закрыт", null);
        } catch (Exception ex) {
            return buildRedirect(engine, "owner/dashboard", null, ex.getMessage());
        }
    }

    @PostMapping("/admin/auctions/{id}/close")
    public String closeAdminAuction(@PathVariable Long id, Authentication authentication, HttpServletRequest request) {
        String engine = resolveEngine(request, authentication, "admin/dashboard");
        try {
            auctionFacade.closeAuctionByAdmin(id);
            return buildRedirect(engine, "admin/dashboard", "Аукцион закрыт администратором", null);
        } catch (Exception ex) {
            return buildRedirect(engine, "admin/dashboard", null, ex.getMessage());
        }
    }

    @PostMapping("/admin/users/{id}/status")
    public String changeUserStatus(@PathVariable Long id, @RequestParam boolean enabled, Authentication authentication, HttpServletRequest request) {
        String engine = resolveEngine(request, authentication, "admin/dashboard");
        try {
            auctionFacade.changeUserEnabled(authentication.getName(), id, enabled);
            return buildRedirect(engine, "admin/dashboard", enabled ? "Пользователь разблокирован" : "Пользователь заблокирован", null);
        } catch (Exception ex) {
            return buildRedirect(engine, "admin/dashboard", null, ex.getMessage());
        }
    }

    @PostMapping("/admin/users/{id}/role")
    public String changeUserRole(@PathVariable Long id, @RequestParam Role role, Authentication authentication, HttpServletRequest request) {
        String engine = resolveEngine(request, authentication, "admin/dashboard");
        try {
            auctionFacade.changeUserRole(authentication.getName(), id, role);
            return buildRedirect(engine, "admin/dashboard", "Роль пользователя обновлена", null);
        } catch (Exception ex) {
            return buildRedirect(engine, "admin/dashboard", null, ex.getMessage());
        }
    }

    @PostMapping("/bids")
    public String placeBid(@ModelAttribute BidRequest request, Authentication authentication, HttpServletRequest httpServletRequest) {
        String engine = resolveEngine(httpServletRequest, authentication, "auctions");
        try {
            auctionFacade.placeBid(request, authentication.getName());
            return buildRedirect(engine, "auctions", "Ставка принята", null);
        } catch (Exception ex) {
            return buildRedirect(engine, "auctions", null, ex.getMessage());
        }
    }

    private void fillCommonModel(Model model, Authentication authentication, HttpServletRequest request, String engine, String pageKey) {
        model.addAttribute("canCreateAuction", hasRole(authentication, "ROLE_OWNER"));
        model.addAttribute("isAdmin", hasRole(authentication, "ROLE_ADMIN"));
        model.addAttribute("isOwner", hasRole(authentication, "ROLE_OWNER"));
        String username = authentication != null ? authentication.getName() : null;
        model.addAttribute("currentUser", username);
        model.addAttribute("currentUsername", username);
        model.addAttribute("errorMessage", request.getParameter("error"));
        model.addAttribute("successMessage", request.getParameter("success"));
        model.addAttribute("engine", engine);
        model.addAttribute("pageTitle", titleFor(pageKey));
        fillEngineSwitch(model, engine, pageKey);
    }

    private void fillEngineSwitch(Model model, String engine, String pageKey) {
        model.addAttribute("currentEngine", engine);
        model.addAttribute("thymeleafUrl", switchUrl("thymeleaf", pageKey));
        model.addAttribute("mustacheUrl", switchUrl("mustache", pageKey));
        model.addAttribute("freemarkerUrl", switchUrl("freemarker", pageKey));
    }

    private String titleFor(String pageKey) {
        return switch (pageKey) {
            case "profile" -> "Личный кабинет";
            case "owner/dashboard" -> "Кабинет владельца";
            case "admin/dashboard" -> "Панель администратора";
            default -> pageKey.contains("/bids") ? "История ставок" : "Аукционы";
        };
    }

    private String switchUrl(String engine, String pageKey) {
        return "/" + engine + "/" + pageKey;
    }

    private boolean hasRole(Authentication authentication, String role) {
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(role::equals);
    }

    private String resolveEngine(HttpServletRequest request, Authentication authentication, String fallbackPage) {
        String referer = request.getHeader("Referer");
        if (referer != null) {
            if (referer.contains("/mustache/")) {
                return "mustache";
            }
            if (referer.contains("/freemarker/")) {
                return "freemarker";
            }
        }
        return "thymeleaf";
    }

    private String buildRedirect(String engine, String page, String successMessage, String errorMessage) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/" + engine + "/" + page);
        if (successMessage != null && !successMessage.isBlank()) {
            builder.queryParam("success", successMessage);
        }
        if (errorMessage != null && !errorMessage.isBlank()) {
            builder.queryParam("error", errorMessage);
        }
        return "redirect:" + builder.build().encode().toUriString();
    }

    private String view(String engine, String name) {
        if ("thymeleaf".equals(engine)) {
            if ("profile".equals(name)) {
                return "thymeleaf/profile/index";
            }
            if ("owner-dashboard".equals(name)) {
                return "thymeleaf/owner/dashboard";
            }
            if ("owner-bids".equals(name)) {
                return "thymeleaf/owner/bids";
            }
            if ("admin-dashboard".equals(name)) {
                return "thymeleaf/admin/dashboard";
            }
        }
        if ("admin-dashboard".equals(name)) {
            return engine + "/admin/dashboard";
        }
        return engine + "/" + name;
    }
}
