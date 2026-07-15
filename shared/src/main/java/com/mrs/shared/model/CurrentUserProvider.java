package com.mrs.shared.model;

import java.util.Optional;

public interface CurrentUserProvider {
    Optional<LoggedUser> get();
}
