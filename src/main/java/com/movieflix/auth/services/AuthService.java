package com.movieflix.auth.services;


import com.movieflix.auth.entities.RefreshToken;
import com.movieflix.auth.entities.User;
import com.movieflix.auth.entities.UserRole;
import com.movieflix.auth.repositories.UserRepository;
import com.movieflix.auth.utils.AuthResponse;
import com.movieflix.auth.utils.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
public class AuthService {
    private  final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    public AuthResponse register(RegisterRequest registerRequest){


        var user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .userName(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .build();


        User savedUser = userRepository.save(user);
        var accessTokken =jwtService.generateToken(savedUser);
        var RefreshToken = refreshTokenService.createRefreshToken(savedUser.getEmail());




  return AuthResponse.builder()
          .accessToken(accessTokken)
          .RefreshToken(RefreshToken.getRefreshToken())
           .build();
    }
}
