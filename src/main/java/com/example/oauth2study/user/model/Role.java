package com.example.oauth2study.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    GUEST("ROLE_GUEST","비회원"),
    USER("ROLE_USER","회원"),
    SELLER("ROLE_SELLER","판매자"),
    ADMIN("ROLE_ADMIN","관리자");

    private final String key;
    private final String title;
}
