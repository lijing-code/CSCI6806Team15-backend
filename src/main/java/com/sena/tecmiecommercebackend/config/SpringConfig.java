package com.sena.tecmiecommercebackend.config;

<<<<<<< HEAD
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
=======
import java.util.Collections;
import java.util.List;
>>>>>>> c9a7f7a (add Google Account Auto login)

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
<<<<<<< HEAD
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

=======
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.sena.tecmiecommercebackend.repository.entity.AuthenticationToken;
import com.sena.tecmiecommercebackend.repository.entity.User;
import com.sena.tecmiecommercebackend.security.JwtTokenProvider;
import com.sena.tecmiecommercebackend.service.AuthenticationService;
>>>>>>> c9a7f7a (add Google Account Auto login)
import com.sena.tecmiecommercebackend.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SpringConfig {

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

<<<<<<< HEAD
=======
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationService authenticationService;

>>>>>>> c9a7f7a (add Google Account Auto login)
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
<<<<<<< HEAD
                        userInfoEndpoint.userService(customOAuth2UserService)  // 设置自定义OAuth2用户服务
                    )
                    .successHandler(customAuthenticationSuccessHandler())
=======
                        userInfoEndpoint.userService(customOAuth2UserService)  // 使用自定义 OAuth2 用户服务
                    )
                    .successHandler(customAuthenticationSuccessHandler())  // 使用自定义成功处理器
>>>>>>> c9a7f7a (add Google Account Auto login)
            );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
<<<<<<< HEAD
        return (HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication) -> {
            // 登录成功后重定向到主页
            response.sendRedirect("/");
=======
        return (request, response, authentication) -> {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email");
            List<String> roles = Collections.singletonList("USER"); // 默认角色
    
            // 生成 JWT Token
            String jwtToken = jwtTokenProvider.createToken(email, roles);
    
            // 保存 JWT Token 到数据库
            User user = authenticationService.getUserByEmail(email);
            if (user == null) {
                user = new User(email); // 假设您有 User 实体和 userRepository
                authenticationService.saveUser(user);
            }
    
            AuthenticationToken authToken = new AuthenticationToken(jwtToken, user);
            authenticationService.saveConfirmationToken(authToken);
    
            // 将 token 添加到响应头或重定向 URL 中
            response.setHeader("Authorization", "Bearer " + jwtToken);
            response.sendRedirect("/?token=" + jwtToken); // 重定向到主页，并携带 token
>>>>>>> c9a7f7a (add Google Account Auto login)
        };
    }
}