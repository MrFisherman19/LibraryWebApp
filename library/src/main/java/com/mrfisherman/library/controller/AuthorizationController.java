package com.mrfisherman.library.controller;

import com.mrfisherman.library.model.dto.LoginCredentials;
import com.mrfisherman.library.model.dto.RegisterCredentials;
import com.mrfisherman.library.service.auth.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthorizationController {

    private final RegistrationService registrationService;

    public AuthorizationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/login")
    //todo validation on fields
    public void login(@RequestBody LoginCredentials loginCredentials){
        throw new IllegalStateException("This method should not be called. It is implemented by Spring Security.");
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    //todo validation on fields
    public void register(@RequestBody RegisterCredentials registerCredentials) {
        registrationService.registerUser(registerCredentials);
    }

    @GetMapping("/registrationConfirm")
    @ResponseStatus(HttpStatus.OK)
    public String verifyToken(@RequestParam String token) {
        return "Hello token :)" + token;
    }
}
