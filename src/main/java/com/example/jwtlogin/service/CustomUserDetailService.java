package com.example.jwtlogin.service;

import com.example.jwtlogin.domain.entity.Member;
import com.example.jwtlogin.domain.repository.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 인증이 수행될 때 호출된다.
     * 검증을 위한 유저 객체를 가져온다.
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // db에서 유저를 조회한 뒤 UserDetails 형태로 바꿔준다. 이 때 비밀번호 인코딩이 수행된다.
        // db에 아예 인코딩된 비밀번호를 저장해도 된다.
        return memberRepository.findByMemberId(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));
    }

    // todo : user, userdetails 동작
    // todo : authenticate 메소드의 세부 프로세스
    private UserDetails createUserDetails(Member member) {
        return User.builder()
                .username(member.getUsername())
                .password(passwordEncoder.encode(member.getPassword()))
                .roles(member.getRoles().toArray(new String[0])) // todo : getRoles()
                .build();
    }

}
