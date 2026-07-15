package com.mrs.security.component;

import com.mrs.shared.model.CurrentUserProvider;
import com.mrs.shared.model.LoggedUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthUserProvider implements CurrentUserProvider {
    @Override
    public Optional<LoggedUser> get() {
        return Optional.ofNullable(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                .map(a -> (LoggedUser) a.getPrincipal());
    }
}
