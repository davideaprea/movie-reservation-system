package com.mrs.security.dto;

import com.mrs.security.entity.User;
import com.mrs.shared.model.Role;
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
                new SimpleGrantedAuthority("ROLE_" + user.getRole())
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

    public Long getId() {
        return user.getId();
    }

    public Long getCinemaId() {
        return user.getCinemaId();
    }

    public Role getRole() {
        return user.getRole();
    }
}
