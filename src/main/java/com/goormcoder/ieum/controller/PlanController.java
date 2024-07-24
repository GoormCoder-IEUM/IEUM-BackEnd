package com.goormcoder.ieum.controller;


import com.goormcoder.ieum.dto.request.PlanCreateDto;
import com.goormcoder.ieum.dto.response.DestinationFindDto;
import com.goormcoder.ieum.dto.response.PlanInfoDto;
import com.goormcoder.ieum.dto.response.PlanSortDto;
import com.goormcoder.ieum.service.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/plans")
@Tag(name = "Plan", description = "일정 관련 API")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping
    @Operation(summary = "여행지 목록 조회", description = "여행지 목록을 조회합니다.")
    public ResponseEntity<List<DestinationFindDto>> getAllDestination() {
        return ResponseEntity.status(HttpStatus.OK).body(planService.getAllDestination());
    }

    @PostMapping
    @Operation(summary = "일정 생성", description = "일정을 생성합니다. 이동수단(vehicle) 유형 - PUBLIC_TRANSPORTATION 또는 OWN_CAR")
    public ResponseEntity<PlanInfoDto> createPlan(@Valid @RequestBody PlanCreateDto planCreateDto) {
        UUID memberId = getMemberId();
        return ResponseEntity.status(HttpStatus.OK).body(planService.createPlan(planCreateDto, memberId));
    }

    private UUID getMemberId() {
        return UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

    @GetMapping
    @Operation(summary = "전체 일정 조회", description = "모든 여행 일정을 조회합니다.")
    public ResponseEntity<List<PlanSortDto>> getAllPlans() {
        List<PlanSortDto> plans = planService.getAllPlans();
        return ResponseEntity.status(HttpStatus.OK).body(plans);
    }

    @GetMapping("/sorted")
    @Operation(summary = "최신순으로 일정 조회", description = "최신순으로 정렬된 일정을 조회합니다.")
    public ResponseEntity<List<PlanSortDto>> getAllPlansSortedByStartDate() {
        List<PlanSortDto> plans = planService.getAllPlansSortedByStartDate();
        return ResponseEntity.status(HttpStatus.OK).body(plans);
    }

    @GetMapping("/sorted/{destinationName}")
    @Operation(summary = "지역별 일정 조회", description = "특정 지역의 일정을 최신순으로 정렬하여 조회합니다.")
    public ResponseEntity<List<PlanSortDto>> getPlansByDestinationSortedByStartDate(@PathVariable String destinationName) {
        List<PlanSortDto> plans = planService.getPlansByDestinationSortedByStartDate(destinationName);
        return ResponseEntity.status(HttpStatus.OK).body(plans);
    }

}
