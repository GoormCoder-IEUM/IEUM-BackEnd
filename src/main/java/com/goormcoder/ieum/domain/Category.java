package com.goormcoder.ieum.domain;

import com.goormcoder.ieum.domain.enumeration.CategoryType;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private CategoryType categoryType;

    @Builder
    private Category(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public static Category of(CategoryType categoryType) {
        return Category.builder()
                .categoryType(categoryType)
                .build();
    }

}
