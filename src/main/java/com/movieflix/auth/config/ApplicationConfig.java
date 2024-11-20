package com.movieflix.auth.config;


import com.movieflix.auth.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {


    private final UserRepository userRepository;

    public ApplicationConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//getting details by username
// by default we don't have findByUsername so we have to define in userdetailsservice with convention findBy

    @Bean
    public UserDetailsService userDetailsService(){
        return username ->userRepository.findByEmail(username).
                orElseThrow(()->new UsernameNotFoundException("user not found  with the email : "+username));
    }


//authenticating the user by authentication provider
    //dao provider
    //we set the userdetails and password encoder

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider  daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

// to process the authentication request given by user
    // the authenticationManager then calls authentication provider to process the request

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //decripted password generator
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();//a techinque

    }



}
