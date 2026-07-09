package com.mrs.app.location.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Immutable;

@AllArgsConstructor
@Entity
@Table(name = "cities")
@Immutable
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true)
    public String name;

    @Column(nullable = false, unique = true)
    private String zipCode;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public Region region;
}
