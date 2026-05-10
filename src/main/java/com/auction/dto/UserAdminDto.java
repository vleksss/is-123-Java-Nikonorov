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
    private boolean canToggleEnabled;
    private boolean canChangeRole;

    public boolean isProtectedUser() {
        return !canToggleEnabled && !canChangeRole;
    }

    public boolean getProtectedUser() {
        return isProtectedUser();
    }

    public boolean isNextEnabledValue() {
        return !enabled;
    }

    public boolean getNextEnabledValue() {
        return isNextEnabledValue();
    }

    public String getStatusActionLabel() {
        return enabled ? "Заблокировать" : "Разблокировать";
    }
}

