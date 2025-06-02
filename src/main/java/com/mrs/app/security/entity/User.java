package com.mrs.app.security.entity;

import com.mrs.app.security.enumeration.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Roles role;

    public static User create(String email, String password) {
        return new User(null, email, password, Roles.USER);
    }

    public static User createWithId(long id) {
        return new User(id, null, null, null);
    }
}
