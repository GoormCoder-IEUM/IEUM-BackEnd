package com.goormcoder.ieum.repository;

import com.goormcoder.ieum.domain.Member;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

    boolean existsByLoginId(String loginId);

    Optional<Member> findByLoginId(String loginId);

    List<Member> findAllByLoginIdContainingAndIdIsNot(String loginId, UUID memberId);

}
