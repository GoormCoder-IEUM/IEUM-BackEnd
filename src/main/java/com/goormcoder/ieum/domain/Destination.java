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

    @Builder
    private Destination(DestinationType destinationType) {
        this.destinationType = destinationType;
    }

    public static Destination of(DestinationType destinationType) {
        return Destination.builder()
                .destinationType(destinationType)
                .build();
    }

}
