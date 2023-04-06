package com.example.jwtlogin.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class TokenInfo {

    // JWT 의 인증 타입 Bearer
    private String grantType;
    private String accessToken;
    private String refreshToken;

    @Builder
    public TokenInfo(String grantType, String accessToken, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;;
    }


}
