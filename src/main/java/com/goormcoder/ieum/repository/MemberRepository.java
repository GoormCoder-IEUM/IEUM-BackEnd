package com.goormcoder.ieum.repository;

import com.goormcoder.ieum.domain.Member;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

    boolean existsByLoginId(String loginId);

    Optional<Member> findByLoginId(String loginId);

    @Query("SELECT m FROM Member m WHERE m.loginId LIKE %:keyword% AND m.id <> :memberId")
    List<Member> findAllByLoginIdContaining(@Param("keyword") String keyword, @Param("memberId") UUID memberId);

}
