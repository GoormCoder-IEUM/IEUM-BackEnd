package com.goormcoder.ieum.repository;

import com.goormcoder.ieum.domain.Destination;
import com.goormcoder.ieum.domain.enumeration.DestinationName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {

    Boolean existsByDestinationName(DestinationName destinationName);

}
