package com.mrs.app.location.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cinemas", uniqueConstraints = {
        @UniqueConstraint(name = "uk_address", columnNames = {"zip_code", "name", "number"})
})
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Address address;

    @Column(nullable = false, unique = true)
    private String name;

    @Embeddable
    public static final class Address {
        @Column(nullable = false)
        private String city;

        @Column(nullable = false)
        private String zipCode;

        @Column(nullable = false)
        private String name;

        @Column(nullable = false)
        private String number;
    }
}
