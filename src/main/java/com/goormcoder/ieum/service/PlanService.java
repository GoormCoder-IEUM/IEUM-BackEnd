package com.goormcoder.ieum.service;

import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.domain.Plan;
import com.goormcoder.ieum.domain.PlanMember;
import com.goormcoder.ieum.dto.request.PlanCreateDto;
import com.goormcoder.ieum.repository.PlanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

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

    @Transactional
    public void createPlanMember(Long planId, String[] memberLoginIds) {
        Plan plan = findByPlanId(planId);

        List<Member> members = Stream.of(memberLoginIds)
                .map(memberService::findByLoginId)
                .toList();

        for(Member member : members) {
            plan.addPlanMember(PlanMember.of(plan, member));
        }

        System.out.println(plan.getPlanMembers());

        planRepository.save(plan);
    }

    private Plan findByPlanId(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("해당 일정은 존재하지 않습니다."));
    }

}
