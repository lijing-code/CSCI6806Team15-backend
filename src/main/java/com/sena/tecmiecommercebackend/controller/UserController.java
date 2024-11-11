package com.sena.tecmiecommercebackend.controller;

<<<<<<< HEAD
=======
import java.util.Collections;
>>>>>>> c9a7f7a (add Google Account Auto login)
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sena.tecmiecommercebackend.dto.ResponseDto;
import com.sena.tecmiecommercebackend.dto.user.RegisterDto;
import com.sena.tecmiecommercebackend.dto.user.SignInDto;
import com.sena.tecmiecommercebackend.dto.user.SignInResponseDto;
import com.sena.tecmiecommercebackend.exceptions.AuthenticationFailException;
import com.sena.tecmiecommercebackend.exceptions.CustomException;
import com.sena.tecmiecommercebackend.repository.IUserRepository;
import com.sena.tecmiecommercebackend.repository.entity.User;
import com.sena.tecmiecommercebackend.security.JwtTokenProvider;
import com.sena.tecmiecommercebackend.service.AuthenticationService;
import com.sena.tecmiecommercebackend.service.UserService;

@RestController
@RequestMapping("/users")
@EnableWebSecurity
<<<<<<< HEAD

=======
>>>>>>> c9a7f7a (add Google Account Auto login)
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // 用于生成 JWT 的工具类

    @GetMapping("/all")
    public List<User> findAllUser(@RequestParam("token") String token) throws AuthenticationFailException {
        authenticationService.authenticate(token);
        return userRepository.findAll();
    }

    @PostMapping("/signup")
    public ResponseDto register(@RequestBody RegisterDto registerDto) throws CustomException {
        return userService.register(registerDto);
    }

    @PostMapping("/signin")
    public SignInResponseDto signIn(@RequestBody SignInDto signInDto) {
        // 检查用户是否存在
        User user = userRepository.findByEmail(signInDto.getEmail());
        if (user == null) {
            throw new CustomException("User not found.");
        }

        // 检查是否为 OAuth 用户
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            // OAuth 用户无密码，直接生成 JWT
            List<String> roles = Collections.singletonList("USER"); // 默认角色，可根据需求调整
            String jwtToken = jwtTokenProvider.createToken(user.getEmail(), roles);
            return new SignInResponseDto("OAuth Login Successful", jwtToken);
        }

        // 非 OAuth 用户则需要进行密码验证
        SignInResponseDto responseDto = userService.signIn(signInDto);
        return responseDto;
    }
}