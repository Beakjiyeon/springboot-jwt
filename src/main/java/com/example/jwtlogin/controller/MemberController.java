package com.example.jwtlogin.controller;

import com.example.jwtlogin.dto.MemberLoginDto;
import com.example.jwtlogin.dto.TokenInfo;
import com.example.jwtlogin.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.example.jwtlogin.security.SecurityUtil.getCurrentMemberId;

@Slf4j
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public TokenInfo login(@RequestBody MemberLoginDto memberLoginDto) {
        String memberId = memberLoginDto.getMemberId();
        String memberPwd = memberLoginDto.getMemberPwd();
        log.info("### memberId: " + memberId);
        log.info("### memberPwd: " + memberPwd);
        TokenInfo tokenInfo = memberService.login(memberId, memberPwd);
        return tokenInfo;
    }

    @PostMapping("/test")
    public String test() {
        return "Hi, " + getCurrentMemberId() + ". You have been authorized since you login";
    }
}
