package com.mrs.app.location.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "addresses", uniqueConstraints = {
        @UniqueConstraint(name = "uk_address", columnNames = {"city_id", "name", "number"})
})
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private City city;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String number;
}
