package com.example.bookstorefull.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Slf4j
public class AuthSecurityAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtTokenService customJwtTokenService;
    @Override
    public void configure(HttpSecurity builder){
        builder.addFilterBefore(new CustomSecurityFilter(customJwtTokenService), UsernamePasswordAuthenticationFilter.class);
    }
}
