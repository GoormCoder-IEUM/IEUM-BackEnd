package com.goormcoder.ieum.repository;

import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.domain.Place;
import com.goormcoder.ieum.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    Place findByPlaceNameAndMemberAndPlanAndDeletedAtIsNull(String placeName, Member member, Plan plan);

    Boolean existsByPlaceNameAndMemberAndPlanAndDeletedAtIsNull(String placeName, Member member, Plan plan);

    Optional<Place> findByIdAndDeletedAtIsNull(Long id);

    List<Place> findByMemberAndPlanAndActivatedAtIsNullAndDeletedAtIsNull(Member member, Plan plan);

    List<Place> findByPlanAndActivatedAtIsNotNullAndDeletedAtIsNull(Plan plan);

    Boolean existsByPlanAndPlaceNameAndActivatedAtIsNotNullAndDeletedAtIsNull(Plan plan, String placeName);

    @Query("SELECT p FROM Place p WHERE p.plan = :plan AND CAST(p.startedAt AS DATE) = :date AND p.startedAt IS NOT NULL AND p.activatedAt IS NOT NULL AND p.deletedAt IS NULL ORDER BY p.startedAt")
    List<Place> findByPlanAndDate(@Param("plan") Plan plan, @Param("date") LocalDate date);

}
