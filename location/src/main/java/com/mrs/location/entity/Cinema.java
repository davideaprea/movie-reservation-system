package com.mrs.location.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cinemas", uniqueConstraints = {
        @UniqueConstraint(name = "uk_cinema_address", columnNames = {"address_id"})
})
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Address address;

    @Column(nullable = false, unique = true)
    private String name;
}
