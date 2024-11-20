package com.movieflix.auth.services;

import com.movieflix.auth.entities.RefreshToken;
import com.movieflix.auth.entities.User;
import com.movieflix.auth.repositories.RefreshTokenRepository;
import com.movieflix.auth.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


@Service
public class RefreshTokenService {



    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    //injecting the refreshtoken and userrepository
    //using constructor dependency injection

    public RefreshTokenService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(String userName){

        //fetching user by username
        User user=  userRepository.findByEmail(userName)
                .orElseThrow(()->new UsernameNotFoundException("user not found withe email "+userName));
        RefreshToken refreshToken = user.getRefreshToken();

        //checking if refresh token already exists for the user
        //if not we are providing refresh token with its uuid for a certain time
        //we have to secure refresh token more compared to access token
        // access token lasts for a small amount of time so there is chance hackers go for refresh token

        if(refreshToken == null) {
            long refreshTokenValidity =    30*1000;
            // 5*60*60*10000;
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())//creating a refreshtoken
                    .expirationTime(Instant.now().plusMillis(refreshTokenValidity))//giving the time of existance
                    .user(user).build();//building for the specified user
            refreshTokenRepository.save(refreshToken);  // saving the refresh token

        }
        return refreshToken;

    }


    public RefreshToken verifyRefreshToken(String refreshToken){


        RefreshToken refToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()-> new RuntimeException("Refresh Token Not Found"));


        if(refToken.getExpirationTime().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(refToken);
            throw new RuntimeException("Refresh Token Expired");
        }
        return refToken;
    }
}
