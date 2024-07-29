package com.goormcoder.ieum.service;

import com.goormcoder.ieum.domain.Destination;
import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.domain.Plan;
import com.goormcoder.ieum.domain.PlanMember;
import com.goormcoder.ieum.domain.enumeration.DestinationName;
import com.goormcoder.ieum.dto.request.PlanCreateDto;
import com.goormcoder.ieum.dto.response.DestinationFindDto;
import com.goormcoder.ieum.dto.response.PlanFindDto;
import com.goormcoder.ieum.dto.response.PlanInfoDto;
import com.goormcoder.ieum.dto.response.PlanSortDto;
import com.goormcoder.ieum.exception.ErrorMessages;
import com.goormcoder.ieum.repository.DestinationRepository;
import com.goormcoder.ieum.repository.PlanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final DestinationRepository destinationRepository;
    private final MemberService memberService;

    @Transactional
    public List<DestinationFindDto> getAllDestinations() {
        return DestinationFindDto.listOf(destinationRepository.findAll());
    }

    @Transactional
    public PlanInfoDto createPlan(PlanCreateDto dto, UUID memberId) {
        Member member = memberService.findById(memberId);
        Destination destination = destinationRepository.findById(dto.destinationId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.DESTINATION_NOT_FOUND.getMessage()));

        Plan plan = Plan.of(destination, dto.startedAt(), dto.endedAt(), dto.vehicle());
        plan.addPlanMember(PlanMember.of(plan, member));

        return PlanInfoDto.of(planRepository.save(plan));
    }

    @Transactional(readOnly = true)
    public PlanFindDto getPlan(Long planId, UUID memberId) {
        Member member = memberService.findById(memberId);
        Plan plan = findByPlanId(planId);
        validatePlanMember(plan, member);

        return PlanFindDto.of(plan);
    }

    @Transactional(readOnly = true)
    public List<PlanSortDto> listAllPlans(UUID memberId) {
        List<Plan> plans = planRepository.findAll().stream()
                .filter(plan -> plan.getPlanMembers().stream().anyMatch(pm -> pm.getMember().getId().equals(memberId)))
                .collect(Collectors.toList());
        return PlanSortDto.listOf(plans);
    }

    @Transactional(readOnly = true)
    public List<PlanSortDto> listPlansByStartDate(UUID memberId) {
        List<Plan> plans = planRepository.findAllByOrderByStartedAtDesc().stream()
                .filter(plan -> plan.getPlanMembers().stream().anyMatch(pm -> pm.getMember().getId().equals(memberId)))
                .collect(Collectors.toList());
        return PlanSortDto.listOf(plans);
    }

    @Transactional(readOnly = true)
    public List<PlanSortDto> listPlansByDestination(UUID memberId, DestinationName destinationName) {
        List<Plan> plans = planRepository.findByDestination_DestinationNameOrderByStartedAtDesc(destinationName).stream()
                .filter(plan -> plan.getPlanMembers().stream().anyMatch(pm -> pm.getMember().getId().equals(memberId)))
                .collect(Collectors.toList());
        return PlanSortDto.listOf(plans);
    }

    @Transactional(readOnly = true)
    public List<PlanSortDto> listPlansByDestinationAndDateRange(UUID memberId, DestinationName destinationName, LocalDateTime start, LocalDateTime end) {
        List<Plan> plans = planRepository.findByDestination_DestinationNameAndStartedAtBetween(destinationName, start, end).stream()
                .filter(plan -> plan.getPlanMembers().stream().anyMatch(pm -> pm.getMember().getId().equals(memberId)))
                .collect(Collectors.toList());
        return PlanSortDto.listOf(plans);
    }

    public Plan findByPlanId(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.PLAN_NOT_FOUND.getMessage()));
    }

    public void validatePlanMember(Plan plan, Member member) {
        plan.getPlanMembers().stream()
                .filter(planMember -> planMember.getMember().equals(member))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.PLAN_MEMBER_NOT_FOUND.getMessage()));
    }

}
