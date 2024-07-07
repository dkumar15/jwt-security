package com.starhealth.validateJWToken.controllers;

import com.starhealth.validateJWToken.dtos.JWTAuthResponse;
import com.starhealth.validateJWToken.dtos.LoginDto;
import com.starhealth.validateJWToken.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/validate/jwt/token")
public class ValidateJwtTokenController {

    private AuthService authService;

    @Autowired
    public ValidateJwtTokenController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto){
        String token = authService.login(loginDto);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }

    @GetMapping("/test")
    public String validate() {
        return "your token is valid";
    }

    @GetMapping("/testing")
    public String testValidation()  {
        return "testing JWT token validation";
    }
}
