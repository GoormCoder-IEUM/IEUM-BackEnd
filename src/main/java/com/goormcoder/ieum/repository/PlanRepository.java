package com.goormcoder.ieum.repository;

import com.goormcoder.ieum.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findAllByOrderByStartedAtDesc();

    // 특정 지역의 일정을 조회하는 메서드
    List<Plan> findByDestinationNameOrderByStartedAtDesc(String destinationName);

    List<Plan> findByDestinationNameAndDateBetween(String destinationName, LocalDateTime startDate, LocalDateTime endDate);


}
