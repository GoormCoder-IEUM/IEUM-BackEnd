package com.goormcoder.ieum.repository;

import com.goormcoder.ieum.domain.Plan;
import com.goormcoder.ieum.domain.enumeration.DestinationName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    Optional<Plan> findByIdAndDeletedAtIsNull(Long id);

    // 최신순으로 모든 일정 조회
    List<Plan> findAllByOrderByStartedAtDesc();

    // 지역별 최신순 일정 조회
    List<Plan> findByDestination_DestinationNameOrderByStartedAtDesc(DestinationName destinationName);

    // 지역별 일정 조회 (날짜 범위 포함)
    List<Plan> findByDestination_DestinationNameAndStartedAtBetween(DestinationName destinationName, LocalDateTime start, LocalDateTime end);
}
