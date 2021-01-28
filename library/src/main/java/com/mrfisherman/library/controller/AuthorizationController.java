package com.mrfisherman.library.controller;

import com.mrfisherman.library.controller.advice.ApiErrorResponse;
import com.mrfisherman.library.model.dto.LoginCredentials;
import com.mrfisherman.library.model.dto.RegisterCredentials;
import com.mrfisherman.library.service.auth.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class AuthorizationController {

    private final RegistrationService registrationService;
    private final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);

    public AuthorizationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginCredentials loginCredentials) {
        throw new IllegalStateException("This method should not be called. It is implemented by Spring Security.");
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public void register(@RequestBody RegisterCredentials registerCredentials) {
        registrationService.registerUser(registerCredentials);
    }

    @GetMapping("/registrationConfirm")
    @ResponseStatus(HttpStatus.PERMANENT_REDIRECT)
    public void verifyToken(@RequestParam String token, HttpServletResponse response) throws IOException {
        if (registrationService.confirmRegistration(token)) response.sendRedirect("/");
    }

    @ExceptionHandler(value = {IOException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse handleVerifyExceptions(IOException e) {
        logger.error("Can't redirect to login page: " + e);
        return new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Please go to login page!");
    }
}
