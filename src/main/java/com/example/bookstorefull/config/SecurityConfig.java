package com.example.bookstorefull.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenService jwtTokenService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(AbstractHttpConfigurer::disable);
       httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET,"/author/find-all").permitAll());
        httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET,"/author/get/{id}").permitAll());
        httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET,"/author/find-all/{name}").permitAll());
        httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET,"/student/find-all").permitAll());
        httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET,"/test/hello").permitAll());
        httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers("/user/register").permitAll());
       httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/author/findBook/{name}").hasAuthority("AUTHOR"));
       httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.DELETE,"/author/delete/{name}").hasAuthority("AUTHOR"));
       httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.PUT, "/author/updateAuthor/{id}").hasAuthority("AUTHOR"));
        httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/author/createBook").hasAuthority("AUTHOR"));
        httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/student/createStudent").hasAuthority("STUDENT"));
        httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET,"/student/currentlyReadingBooks/{id}").hasAuthority("STUDENT"));
        httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET,"/student/{bookName}").hasAnyAuthority("STUDENT", "AUTHOR"));
        httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/author/{authorName}").hasAnyAuthority("AUTHOR", "STUDENT"));
        httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST,"/author/subscribedSave").hasAuthority("AUTHOR"));
        httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST,"/author/{authorId}/publish").hasAuthority("AUTHOR"));
       httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers(
               "/v3/api-docs/**",
               "/swagger-ui/**",
               "/swagger-ui.html").permitAll());
       httpSecurity.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
       httpSecurity.httpBasic(withDefaults());
//       httpSecurity.apply(new AuthSecurityAdapter(jwtTokenService));
        httpSecurity.addFilterBefore(new CustomSecurityFilter(jwtTokenService), UsernamePasswordAuthenticationFilter.class);
       return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
