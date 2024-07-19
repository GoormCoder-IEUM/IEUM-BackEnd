package com.goormcoder.ieum.repository;

import com.goormcoder.ieum.domain.PlanMember;
import com.goormcoder.ieum.exception.ErrorMessages;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PlanMemberRepository extends JpaRepository<PlanMember, Long> {

    default PlanMember findByMemberIdAndPlanIdOrThrow(final UUID memberId, final Long planId) {
        return findByMemberIdAndPlanId(memberId, planId).orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MEMBER_NOT_FOUND.getMessage()));
    }

    Optional<PlanMember> findByMemberIdAndPlanId(UUID memberId, Long planId);

}
