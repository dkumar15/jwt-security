package com.starhealth.validateJWToken.services;

import com.starhealth.validateJWToken.config.JwtTokenProvider;
import com.starhealth.validateJWToken.dtos.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(LoginDto loginDto) {
        // if(userRepository.existsByUsername(loginDto.getUsernameOrEmail()) ||  userRepository.existsByEmail(loginDto.getUsernameOrEmail())) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return token;
//        } else {
//            throw new AuthenticationAPIException(HttpStatus.BAD_REQUEST, "User name or Email does not exists!.");
//        }
    }
}
