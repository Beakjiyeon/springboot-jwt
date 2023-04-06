package com.example.jwtlogin.service;

import com.example.jwtlogin.domain.repository.MemberRepository;
import com.example.jwtlogin.dto.TokenInfo;
import com.example.jwtlogin.security.JwtTokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;


    public MemberService(MemberRepository memberRepository, AuthenticationManagerBuilder authenticationManagerBuilder, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 로그인 서비
     * @param memberId
     * @param pwd
     * @return
     */
    @Transactional
    public TokenInfo login(String memberId, String pwd) {
        // id/pw 기반으로 인증 객체를 생성한다. 인증 상태 = false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, pwd);

        // 실제 검증 : 비밀번호 체크
        // authenticate() -> CustomUserDetailsService 의 loadUserByUsername()
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보 기반으로 Jwt 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        return tokenInfo;
    }
}
