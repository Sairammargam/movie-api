package com.movieflix.auth.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name ="users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder

public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer userId;

    @NotBlank(message = "name field cannot be blank")
    private String name;

    @NotBlank(message = "username  field cannot be blank")
    private String userName;

    @NotBlank(message = "email field cannot be blank")
    @Column(unique = true)
    @Email(message = "please enter the correcr mail")
    private String email;

    @NotBlank(message = "please enter the password")
    @Size(min=8, message = "the password must have atleast 8 characters")
    private String password;


    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;

@Enumerated(EnumType.STRING)
    private UserRole role;

private boolean isEnabled = true;

private boolean isAccountNonExpired=true;

 private boolean  isCredentialsNonExpired = true;

 private boolean   isAccountNonLocked=true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
