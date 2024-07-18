package com.goormcoder.ieum.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false, unique = true)
    private String name;

    @Builder
    private Category(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Category of(String code, String name) {
        return Category.builder()
                .code(code)
                .name(name)
                .build();
    }

}
