package com.augusto.employees.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.augusto.employees.payload.AuthResponse;
import com.augusto.employees.payload.LoginDto;
import com.augusto.employees.security.JwtTokenProvider;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("singing")
    public AuthResponse authenticateEmployee(@RequestBody LoginDto login) {

        var authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(login.getCpfOrEmail(), login.getPassword()));
        String token = tokenProvider.generateToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new AuthResponse(token);
    }
}
