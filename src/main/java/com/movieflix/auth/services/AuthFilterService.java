package com.movieflix.auth.services;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//validates the access tokens
@Service
public class AuthFilterService  extends OncePerRequestFilter {


    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public AuthFilterService(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull  HttpServletRequest request,
                                    @NonNull  HttpServletResponse response,
                                    @NonNull  FilterChain filterChain) throws ServletException, IOException {

         //authorization header
       final  String authHeader = request.getHeader("Authorization");
       //header by default start with "Bearer "(it is the convention)
       if(authHeader==null || !authHeader.startsWith("Bearer ")){
           filterChain.doFilter(request,response);
           return;
       }
//extract jwt token
       String jwt = authHeader.substring(7);

//extract username from jwt token
        String username = jwtService.extractUsername(jwt);


        //username not null && user is not authonticated yet or not

      if(username!= null && SecurityContextHolder.getContext().getAuthentication()==null){
          UserDetails userDetails= userDetailsService.loadUserByUsername(username);
          //checking token is valid or not
          if(jwtService.isTokenValid(jwt,userDetails)){
              UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                      userDetails,
                      null,
                      userDetails.getAuthorities()//gives what permissions does the user have  ex: user or admin

              );
              authenticationToken.setDetails(
                      new WebAuthenticationDetailsSource().buildDetails(request) //to build whole authentication object
              );
              SecurityContextHolder.getContext().setAuthentication(authenticationToken); // authenticated
          }

      }
      filterChain.doFilter(request,response);//go for next chain of filters
    }

}
