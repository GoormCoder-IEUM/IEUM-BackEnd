package com.goormcoder.ieum.repository;

import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.domain.Place;
import com.goormcoder.ieum.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    Place findByPlaceNameAndMemberAndPlanAndDeletedAtIsNull(String placeName, Member member, Plan plan);

    Boolean existsByPlaceNameAndMemberAndPlanAndDeletedAtIsNull(String placeName, Member member, Plan plan);

    Optional<Place> findByIdAndDeletedAtIsNull(Long id);

    List<Place> findByMemberAndPlanAndActivatedAtIsNullAndDeletedAtIsNull(Member member, Plan plan);

    List<Place> findByPlanAndActivatedAtIsNotNullAndDeletedAtIsNull(Plan plan);

}
