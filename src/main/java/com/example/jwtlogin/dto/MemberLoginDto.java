package com.example.jwtlogin.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberLoginDto {

    private String memberId;
    private String memberPwd;

    @Builder
    public MemberLoginDto(String memberId, String memberPwd) {
        this.memberId = memberId;
        this.memberPwd = memberPwd;
    }
}
