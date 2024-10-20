package com.example.bookstorefull.service;

import com.example.bookstorefull.config.JwtParser;
import com.example.bookstorefull.dto.RequestRegisterDto;
import com.example.bookstorefull.dto.ResponseRegisterDto;
import com.example.bookstorefull.model.Authority;
import com.example.bookstorefull.model.User;
import com.example.bookstorefull.repository.AuthorityRepository;
import com.example.bookstorefull.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtParser jwtParser;

    public ResponseRegisterDto register(RequestRegisterDto requestRegisterDto){
        log.info("requestRegisterDto : {} ", requestRegisterDto);
       userRepository.findByName(requestRegisterDto.getName()).ifPresent(user -> {
           throw new RuntimeException("User not found!");
       });
       if (!requestRegisterDto.getPassword().equals(requestRegisterDto.getRepeatPassword())){
           throw new RuntimeException("Password is not match");
       }
       List<Authority>authorities = new ArrayList<>();
       requestRegisterDto.getAuthorities().forEach(s -> {
           authorityRepository.findByUserAuthority(s);
           Authority authority = Authority
                   .builder()
                   .userAuthority(s)
                   .build();
           authorities.add(authority);
           log.info("authorities : {} ", authorities);
       });
       User user = User
               .builder()
               .name(requestRegisterDto.getName())
               .password(passwordEncoder.encode(requestRegisterDto.getPassword()))
               .authorities(authorities)
               .build();
       User userSave = userRepository.save(user);
       log.info("userSave : {} ", userSave);
       String token = jwtParser.generateToken(userSave);
       log.info("token : {} ", token);
       return ResponseRegisterDto
               .builder()
               .jwt(token)
               .build();
    }
}
