package com.goormcoder.ieum.repository;

import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    Place findByPlaceNameAndMember(String placeName, Member member);

    Boolean existsByPlaceNameAndMember(String placeName, Member member);

}
