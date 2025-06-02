package com.mrs.app.security.service;

import com.mrs.app.security.entity.User;
import com.mrs.app.security.pojo.AuthUserDetails;
import com.mrs.app.security.repository.UserDao;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthUserDetailsService implements UserDetailsService {
    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        return new AuthUserDetails(user);
    }
}