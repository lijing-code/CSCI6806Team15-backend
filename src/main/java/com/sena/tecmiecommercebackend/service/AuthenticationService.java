package com.sena.tecmiecommercebackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.tecmiecommercebackend.config.AuthenticationMessage;
import com.sena.tecmiecommercebackend.exceptions.AuthenticationFailException;
import com.sena.tecmiecommercebackend.repository.ITokenRepository;
import com.sena.tecmiecommercebackend.repository.IUserRepository;
import com.sena.tecmiecommercebackend.repository.entity.AuthenticationToken;
import com.sena.tecmiecommercebackend.repository.entity.User;
import com.sena.tecmiecommercebackend.security.JwtTokenProvider;
import com.sena.tecmiecommercebackend.utils.Helper;

@Service
public class AuthenticationService {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ITokenRepository tokenRepository;

    @Autowired
    private IUserRepository userRepository;

    public void authenticate(String token) throws AuthenticationFailException {
        System.out.println("Authenticating token: " + token); // Log the received token
        
        if (!Helper.notNull(token)) {
            System.out.println("Token is null or empty."); // Log if token is missing
            throw new AuthenticationFailException(AuthenticationMessage.AUTH_TOEKN_NOT_PRESENT);
        }

        // 尝试使用 JWT Provider 验证 token
        if (jwtTokenProvider.validateToken(token)) {
            System.out.println("Token validated successfully with JwtTokenProvider."); // Log successful JWT validation
            return; // Token 验证成功
        }

        // 如果 JWT 验证失败，则尝试在数据库中查找 token
        User user = getUser(token);
        if (user == null) {
            System.out.println("Authentication failed: User not found for token: " + token); // Log if token not associated with any user
            throw new AuthenticationFailException(AuthenticationMessage.AUTH_TOEKN_NOT_VALID);
        }
        System.out.println("User associated with token: " + user.getEmail()); // Log user associated with token
    }

    public User getUser(String token) {
        System.out.println("Looking up user by token in the database."); // Log database lookup
        AuthenticationToken authenticationToken = tokenRepository.findTokenByToken(token);
        
        if (authenticationToken != null) {
            User user = authenticationToken.getUser();
            if (Helper.notNull(user)) {
                System.out.println("User found in database: " + user.getEmail()); // Log user found in database
                return user;
            } else {
                System.out.println("No user found associated with this token."); // Log if token exists but no user
            }
        } else {
            System.out.println("Token not found in database."); // Log if token not found
        }
        
        return null;
    }

    public void saveConfirmationToken(AuthenticationToken authenticationToken) {
        System.out.println("Saving confirmation token to the database for user: " + authenticationToken.getUser().getEmail());
        tokenRepository.save(authenticationToken);
    }

    public AuthenticationToken getToken(User user) {
        System.out.println("Retrieving token for user: " + user.getEmail());
        return tokenRepository.findTokenByUser(user);
    }

    // 新增方法：根据邮箱获取用户
    public User getUserByEmail(String email) {
        System.out.println("Looking up user by email: " + email); // Log email lookup
        return userRepository.findByEmail(email);
    }

    // 新增方法：保存用户
    public void saveUser(User user) {
        System.out.println("Saving new user with email: " + user.getEmail()); // Log new user creation
        userRepository.save(user);
    }
}