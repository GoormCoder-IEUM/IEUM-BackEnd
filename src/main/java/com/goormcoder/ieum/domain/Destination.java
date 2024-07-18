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

    @Column(nullable = false, unique = true)
    private String destinationKr;

    @Column(nullable = false, unique = true)
    private String destinationEn;

    @Builder
    private Destination(String destinationKr, String destinationEn) {
        this.destinationKr = destinationKr;
        this.destinationEn = destinationEn;
    }

    public static Destination of(String destinationKr, String destinationEn) {
        return Destination.builder()
                .destinationKr(destinationKr)
                .destinationEn(destinationEn)
                .build();
    }

}
