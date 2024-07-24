package com.goormcoder.ieum.repository;

import com.goormcoder.ieum.domain.Plan;
import com.goormcoder.ieum.domain.enumeration.DestinationName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findAllByOrderByStartedAtDesc();
    List<Plan> findByDestinationDestinationNameOrderByStartedAtDesc(DestinationName destinationName);
    List<Plan> findByDestinationDestinationNameAndStartedAtBetween(DestinationName destinationName, LocalDateTime start, LocalDateTime end);
}
