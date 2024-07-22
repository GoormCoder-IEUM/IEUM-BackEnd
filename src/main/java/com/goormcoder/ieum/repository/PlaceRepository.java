package com.goormcoder.ieum.repository;

import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.domain.Place;
import com.goormcoder.ieum.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    Place findByPlaceNameAndMemberAndPlan(String placeName, Member member, Plan plan);

    Boolean existsByPlaceNameAndMemberAndPlan(String placeName, Member member, Plan plan);

}
