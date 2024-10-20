package com.example.bookstorefull.service;

import com.example.bookstorefull.model.User;
import com.example.bookstorefull.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User userDB = userRepository.findByName(name).orElseThrow(()-> new RuntimeException("User not found!"));
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .builder()
                .username(userDB.getName())
                .password(userDB.getPassword())
                .authorities(userDB.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getUserAuthority()))
                        .collect(Collectors.toSet()))
                .build();
        return userDetails;
    }
}
