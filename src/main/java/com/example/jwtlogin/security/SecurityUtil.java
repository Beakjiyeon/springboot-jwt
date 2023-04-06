package com.example.jwtlogin.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    /**
     * api 요청 헤더에 있는 사용자 정보에서 memberId 를 조회
     * @return
     */
    public static String getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("No authentication info");
        }
        return authentication.getName();
    }
}
