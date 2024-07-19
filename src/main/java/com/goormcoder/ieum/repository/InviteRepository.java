package com.goormcoder.ieum.repository;

import com.goormcoder.ieum.domain.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InviteRepository extends JpaRepository<Invite, Long> {

    Optional<Invite> findByMemberIdAndPlanId(UUID memberId, Long planId);

    @Query("SELECT i FROM Invite i WHERE i.member.id = :memberId AND i.plan.id = :planId AND i.deletedAt IS NULL AND i.acceptance IS NULL")
    Optional<Invite> findByMemberIdAndPlanIdAndIsNull(UUID memberId, Long planId);

    List<Invite> findAllByMemberId(UUID memberId);

    List<Invite> findAllByPlanId(Long planId);

}
