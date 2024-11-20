package com.movieflix.auth.config;


import com.movieflix.auth.services.AuthFilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthenticationFilter authenticationFilter;
    private final AuthenticationProvider authenticationProvider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        //csrf - cross site request forgey  by default spring includes csrf protection
        //in stateless we disable the csrf (token based mechanisms or jwt token usage)

        http
                .csrf(AbstractHttpConfigurer :: disable)
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/api/v1/auth")//matchs all end point with the url
                        .permitAll()//permit all with or without authentication
                        .anyRequest()//any other requests other tha request matchers
                        .authenticated())//are to be authenticated
                .sessionManagement(session-> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))//stateless-no session should be created or used
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authenticationFilter , UsernamePasswordAuthenticationFilter.class);

  return http.build();
    }

}
