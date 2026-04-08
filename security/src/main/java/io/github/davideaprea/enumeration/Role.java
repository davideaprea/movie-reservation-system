package io.github.davideaprea.enumeration;

import lombok.Getter;

@Getter
public enum Role {
    OPERATOR("OPERATOR"),
    MEDIC("MEDIC"),
    USER("USER"),
    ADMIN("ADMIN");

    private final String value;

    Role(String role) {
        value = "ROLE_" + role;
    }
}
