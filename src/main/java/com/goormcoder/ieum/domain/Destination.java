package com.goormcoder.ieum.domain;

import com.goormcoder.ieum.domain.enumeration.DestinationName;
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
    private DestinationName destinationName;

    @Column(nullable = false)
    private String description;

    @Builder
    private Destination(DestinationName destinationName, String description) {
        this.destinationName = destinationName;
        this.description = description;
    }

    public static Destination of(DestinationName destinationName, String description) {
        return Destination.builder()
                .destinationName(destinationName)
                .description(description)
                .build();
    }

}
