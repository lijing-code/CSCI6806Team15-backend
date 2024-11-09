package com.sena.tecmiecommercebackend.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.sena.tecmiecommercebackend.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SpringConfig {

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests(authorizeRequests ->
                authorizeRequests
                    .antMatchers("/**", "/signin", "/oauth2/authorization/google", "/css/**", "/js/**", "/images/**").permitAll()
                    .anyRequest().authenticated()
            )
            .oauth2Login(oauth2Login ->
                oauth2Login
                    .loginPage("/signin")
                    .userInfoEndpoint(userInfoEndpoint ->
                        userInfoEndpoint.userService(customOAuth2UserService)  // 设置自定义OAuth2用户服务
                    )
                    .successHandler(customAuthenticationSuccessHandler())
            );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication) -> {
            // 登录成功后重定向到主页
            response.sendRedirect("/");
        };
    }
}