package com.auction.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserAdminDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    private boolean enabled;
}
