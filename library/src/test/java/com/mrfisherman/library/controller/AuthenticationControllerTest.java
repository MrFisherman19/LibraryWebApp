package com.mrfisherman.library.controller;

import com.mrfisherman.library.model.entity.User;
import com.mrfisherman.library.persistence.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Optional;

import static java.lang.String.format;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Should log in and should be able to get secured content")
    @Transactional
    void should_log_in_and_get_secured_content() throws Exception {
        //given
        User user = new User();
        user.setUsername("test");
        user.setPassword("$2y$12$Jtig3XjP4bXa8QwxE3eZm.Q8Xd29c671rS/pwr961qdbsY8pA.LHy");
        user.setEmail("test@test.com");
        user.setEnabled(true);
        userRepository.saveAndFlush(user);
        //when
        MvcResult login = mockMvc.perform(post("/login")
                .content("{\"username\": \"test\", \"password\": \"test\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String token = login.getResponse().getHeader("Authorization");
        //then
        mockMvc.perform(get("/secured")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("secured content"));

        mockMvc.perform(get("/secured"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should not log in and show properly message when user does not exist")
    @Transactional
    void should_not_log_in_when_user_does_not_exist() throws Exception {
        //given
        User user = new User();
        user.setUsername("Test");
        user.setPassword("$2y$12$Jtig3XjP4bXa8QwxE3eZm.Q8Xd29c671rS/pwr961qdbsY8pA.LHy");
        user.setEmail("test@test.com");
        user.setEnabled(true);
        userRepository.saveAndFlush(user);
        //when
        MvcResult result = mockMvc.perform(post("/login")
                .content("{\"username\": \"" + user.getUsername() + "\", \"password\": \"" + user.getPassword() + "\"}"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();
        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(401);
        assertThat(result.getResponse().getErrorMessage()).isEqualTo("Bad credentials");
    }

    @Test
    @DisplayName("Should not log in and show properly message when user does exist but provided wrong password")
    @Transactional
    void should_not_log_in_when_bad_password() throws Exception {
        //given
        String nonExistingUser = "user";
        String nonExistingPassword = "password";
        //when
        MvcResult result = mockMvc.perform(post("/login")
                .content("{\"username\": \"" + nonExistingUser + "\", \"password\": \"" + nonExistingPassword + "\"}"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();
        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(401);
        assertThat(result.getResponse().getErrorMessage())
                .isEqualTo(format("User with given username %s not found!", nonExistingUser));
    }

    @Test
    @Transactional
    void should_register_user() throws Exception {
        //when
        mockMvc.perform(post("/register")
                .contentType("application/json")
                .content("{\"username\": \"test\", \"password\": \"test\", \"email\": \"test@test.pl\"}"))
                .andDo(print())
                .andExpect(status().isOk());
        //then
        Optional<User> loadedUsername = userRepository.findByUsername("test");
        assertThat(loadedUsername).isNotEmpty();
        assertThat(loadedUsername.get().getUsername()).isEqualTo("test");
        assertThat(passwordEncoder.matches("test", loadedUsername.get().getPassword())).isTrue();
        assertThat(loadedUsername.get().getRoles().stream().anyMatch(x->x.getName().equals("USER"))).isTrue();
        assertThat(loadedUsername.get().getEmail()).isEqualTo("test@test.pl");
        assertThat(loadedUsername.get().isEnabled()).isFalse();
    }
}