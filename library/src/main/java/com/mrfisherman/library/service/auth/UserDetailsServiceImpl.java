package com.mrfisherman.library.service.auth;

import com.mrfisherman.library.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).map(
                user -> new User(
                        user.getUsername(),
                        user.getPassword(),
                        user.isEnabled(),
                        user.isAccountNonExpired(),
                        user.isCredentialsNonExpired(),
                        user.isNonLocked(),
                        user.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())))
                .orElseThrow(() -> new EntityNotFoundException(format("User with given username %s not found!", username)));
    }
}
