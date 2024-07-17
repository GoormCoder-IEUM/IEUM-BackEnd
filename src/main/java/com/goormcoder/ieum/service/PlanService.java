package com.goormcoder.ieum.service;

import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.domain.Plan;
import com.goormcoder.ieum.domain.PlanMember;
import com.goormcoder.ieum.dto.request.PlanCreateDto;
import com.goormcoder.ieum.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final MemberService memberService;

    @Transactional
    public void createPlan(PlanCreateDto dto, UUID memberId) {
        Member member = memberService.findById(memberId);
        Plan plan = Plan.of(dto.location(), dto.startedAt(), dto.endedAt(), dto.vehicle());
        plan.addPlanMember(PlanMember.of(plan, member));
        planRepository.save(plan);
    }

}
