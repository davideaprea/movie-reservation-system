package com.example.demo.security.pojo;

import com.example.demo.security.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class AuthUserDetails implements UserDetails {
    private User user;

    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(user.getRole().toString())
        );
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }
}
