package com.movieflix.Controllers;

import com.movieflix.auth.utils.AuthResponse;
import com.movieflix.auth.utils.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest){
       return new ResponseEntity<>();
    }
}
