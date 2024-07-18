package com.goormcoder.ieum.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private DestinationType destinationType;

    @Column(nullable = false)
    private String description;

    @Builder
    private Destination(DestinationType destinationType, String description) {
        this.destinationType = destinationType;
        this.description = description;
    }

    public static Destination of(DestinationType destinationType, String description) {
        return Destination.builder()
                .destinationType(destinationType)
                .description(description)
                .build();
    }

}
