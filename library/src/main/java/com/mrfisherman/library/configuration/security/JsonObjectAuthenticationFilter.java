package com.mrfisherman.library.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrfisherman.library.model.dto.LoginCredentials;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class JsonObjectAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try(BufferedReader reader = request.getReader()) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            LoginCredentials loginCredentials = objectMapper.readValue(sb.toString(), LoginCredentials.class);

            var token = new UsernamePasswordAuthenticationToken(loginCredentials.getUsername(), loginCredentials.getPassword());

            setDetails(request, token);

            return this.getAuthenticationManager().authenticate(token);

        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
