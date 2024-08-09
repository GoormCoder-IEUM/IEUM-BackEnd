package com.goormcoder.ieum.repository;

import com.goormcoder.ieum.domain.Category;
import com.goormcoder.ieum.domain.enumeration.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Boolean existsByCategoryType(CategoryType categoryType);

}
