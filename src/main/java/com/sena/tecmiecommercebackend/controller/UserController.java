package com.sena.tecmiecommercebackend.controller;

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
import com.sena.tecmiecommercebackend.service.AuthenticationService;
import com.sena.tecmiecommercebackend.service.UserService;

@RestController
@RequestMapping("/users")
@EnableWebSecurity

public class UserController {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    UserService userService;

    @GetMapping("/all")
    public List<User> findAllUser(@RequestParam("token") String token) throws AuthenticationFailException {
        authenticationService.authenticate(token);
        return userRepository.findAll();
    }

    @PostMapping("/signup")
    public ResponseDto register(@RequestBody RegisterDto registerDto)  throws CustomException {
        return userService.register(registerDto);
    }

    @PostMapping("/signin")
    public SignInResponseDto signIn(@RequestBody SignInDto signInDto) {
        return  userService.signIn(signInDto);
    }
}