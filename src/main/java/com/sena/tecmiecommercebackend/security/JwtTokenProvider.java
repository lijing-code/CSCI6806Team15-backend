package com.sena.tecmiecommercebackend.security;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

    private final String SECRET_KEY = "your-secret-key";

    // 创建 JWT
    public String createToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1-day expiration
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // 验证 JWT
    public boolean validateToken(String token) {
        try {
            // 检查 token 是否来自 Google（例如，使用 iss 字段）
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            System.out.println("Token claims: " + claims);
            if (claims.getIssuer().equals("https://accounts.google.com")) {
                // 如果是 Google 的 token，使用 Google 公钥进行验证
                // 使用 Google 提供的 JWT 验证库或 API 验证 token
                boolean googleValidationResult = validateGoogleToken(token);
                System.out.println("Google token validation result: " + googleValidationResult);
                return googleValidationResult;
            } else {
                // 否则使用本地的 SECRET_KEY 验证
                Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

        // 验证Google token的示例方法
    private boolean validateGoogleToken(String token) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList("YOUR_GOOGLE_CLIENT_ID"))
                    .build();
            
            GoogleIdToken idToken = verifier.verify(token);
            return idToken != null;
        } catch (Exception e) {
            return false;
        }
    }

    // 从 JWT 中获取用户名
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    // 从 JWT 中获取认证信息
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        String username = claims.getSubject();
        List<String> roles = claims.get("roles", List.class);

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(username)
                .authorities(authorities)
                .password("") // password is not necessary here
                .build();

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    // 从请求头中解析 JWT
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}