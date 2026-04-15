package com.auction.controller.api;

import com.auction.dto.UserAdminDto;
import com.auction.facade.AuctionFacade;
import com.auction.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserApiController {
    private final AuctionFacade auctionFacade;

    @GetMapping
    public List<UserAdminDto> getAll() {
        return auctionFacade.getUsersForAdmin();
    }

    @PostMapping("/{id}/status")
    public UserAdminDto changeStatus(@PathVariable Long id, @RequestParam boolean enabled) {
        return auctionFacade.changeUserEnabled(id, enabled);
    }

    @PostMapping("/{id}/role")
    public UserAdminDto changeRole(@PathVariable Long id, @RequestParam Role role) {
        return auctionFacade.changeUserRole(id, role);
    }
}
