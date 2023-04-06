package com.example.jwtlogin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // rest api 이므로 basic auth, csrf 보안을 사용하지 않는다.
                .httpBasic().disable()
                .csrf().disable()

                // jwt 를 이용하기 때문에 세션 사용 안함
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                // 해당 api 에 대해 모든 요청을 허가한다.
                .antMatchers("/members/login").permitAll()
                // USER 권한이 있어야 요청할 수 있다.
                .antMatchers("/members/test").hasRole("USER")
                // 이 밖의 모든 요청은 인증을 필요로 한다.
                .anyRequest().authenticated()


                .and()
                // jwt 인증을 위햐ㅐ 직접 구현한 필터를 특정 필터 전에 수행 하겠다.
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        // jwt 를 사용하기 위해서 기본적으로 패스워드 인코더가 필요한데 이 프로젝트에서는 Bycrypt encoder 을 사용한다.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
