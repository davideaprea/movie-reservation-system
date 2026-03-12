package com.mrs.app.security.enumeration;

import lombok.Getter;

@Getter
public enum Roles {
    OPERATOR("OPERATOR"),
    MEDIC("MEDIC"),
    USER("USER"),
    ADMIN("ADMIN");

    private final String value;

    Roles(String role) {
        value = "ROLE_" + role;
    }
}
