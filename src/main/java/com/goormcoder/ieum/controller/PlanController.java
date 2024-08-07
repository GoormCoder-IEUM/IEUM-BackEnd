package com.goormcoder.ieum.controller;

import com.goormcoder.ieum.domain.enumeration.DestinationName;
import com.goormcoder.ieum.domain.enumeration.PlanVehicle;
import com.goormcoder.ieum.dto.request.PlanCreateDto;
import com.goormcoder.ieum.dto.response.DestinationFindDto;
import com.goormcoder.ieum.dto.response.PlanFindDto;
import com.goormcoder.ieum.dto.response.PlanInfoDto;
import com.goormcoder.ieum.dto.response.PlanSortDto;
import com.goormcoder.ieum.service.KakaoOauthService;
import com.goormcoder.ieum.service.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public ResponseEntity<List<DestinationFindDto>> getAllDestinations() {
        return ResponseEntity.status(HttpStatus.OK).body(planService.getAllDestinations());
    }

    @PostMapping
    @Operation(summary = "일정 생성", description = "일정을 생성합니다. 이동수단(vehicle) 유형 - PUBLIC_TRANSPORTATION 또는 OWN_CAR")
    public ResponseEntity<PlanInfoDto> createPlan(@Valid @RequestBody PlanCreateDto planCreateDto) {
        UUID memberId = getMemberId();
        return ResponseEntity.status(HttpStatus.OK).body(planService.createPlan(planCreateDto, memberId));
    }

    @GetMapping("/{planId}")
    @Operation(summary = "일정 조회", description = "일정을 조회합니다.")
    public ResponseEntity<PlanFindDto> getPlan(@PathVariable Long planId) {
        UUID memberId = getMemberId();
        return ResponseEntity.status(HttpStatus.OK).body(planService.getPlan(planId, memberId));
    }

    @GetMapping("/all")
    @Operation(summary = "전체 일정 조회", description = "모든 여행 일정을 조회합니다.")
    public ResponseEntity<List<PlanSortDto>> listAllPlans() {
        UUID memberId = getMemberId();
        List<PlanSortDto> plans = planService.listAllPlans(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(plans);
    }

    @GetMapping("/sorted")
    @Operation(summary = "최신순으로 일정 조회", description = "최신순으로 정렬된 일정을 조회합니다.")
    public ResponseEntity<List<PlanSortDto>> listPlansByStartDate() {
        UUID memberId = getMemberId();
        List<PlanSortDto> plans = planService.listPlansByStartDate(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(plans);
    }

    @GetMapping("/sorted/{destinationName}")
    @Operation(summary = "지역별 일정 조회", description = "특정 지역의 일정을 최신순으로 정렬하여 조회합니다.")
    public ResponseEntity<List<PlanSortDto>> listPlansByDestination(@PathVariable DestinationName destinationName) {
        UUID memberId = getMemberId();
        List<PlanSortDto> plans = planService.listPlansByDestination(memberId, destinationName);
        return ResponseEntity.status(HttpStatus.OK).body(plans);
    }
    
    @GetMapping("/sorted/{destinationName}/{start}/{end}")
    @Operation(summary = "특정 기간 동안의 지역별 일정 조회", description = "특정 기간 동안 특정 지역의 일정을 조회합니다.")
    public ResponseEntity<List<PlanSortDto>> getPlansByDestinationAndDateRange(@PathVariable DestinationName destinationName, @PathVariable LocalDateTime start, @PathVariable LocalDateTime end) {
        UUID memberId = getMemberId();
        List<PlanSortDto> plans = planService.listPlansByDestinationAndDateRange(memberId, destinationName, start, end);
        return ResponseEntity.status(HttpStatus.OK).body(plans);
    }

    @DeleteMapping("/{planId}")
    @Operation(summary = "일정 삭제", description = "일정을 삭제합니다.")
    public ResponseEntity<String> deletePlan(@PathVariable Long planId) {
        UUID memberId = getMemberId();
        planService.deletePlan(planId, memberId);
        return ResponseEntity.status(HttpStatus.OK).body("일정이 삭제되었습니다.");
    }

    @PutMapping("/{planId}")
    @Operation(summary = "일정 수정", description = "일정을 수정합니다.")
    public ResponseEntity<PlanInfoDto> updatePlan(@PathVariable Long planId, @Valid @RequestBody PlanCreateDto planCreateDto) {
        UUID memberId = getMemberId();
        return ResponseEntity.status(HttpStatus.OK).body(planService.updatePlan(planId, planCreateDto, memberId));
    }

    @PutMapping("/{planId}/destination")
    @Operation(summary = "일정 목적지 변경", description = "일정의 목적지를 변경합니다.")
    public ResponseEntity<PlanInfoDto> changeDestination(@PathVariable Long planId, @RequestParam Long newDestinationId) {
        UUID memberId = getMemberId();
        return ResponseEntity.status(HttpStatus.OK).body(planService.updateDestination(planId, newDestinationId, memberId));
    }

    @PutMapping("/{planId}/start-time")
    @Operation(summary = "일정 시작 시간 변경", description = "일정의 시작 시간을 변경합니다.")
    public ResponseEntity<PlanInfoDto> changeStartTime(@PathVariable Long planId, @RequestParam LocalDateTime newStartTime) {
        UUID memberId = getMemberId();
        return ResponseEntity.status(HttpStatus.OK).body(planService.updateStartTime(planId, newStartTime, memberId));
    }

    @PutMapping("/{planId}/end-time")
    @Operation(summary = "일정 끝나는 시간 변경", description = "일정의 끝나는 시간을 변경합니다.")
    public ResponseEntity<PlanInfoDto> changeEndTime(@PathVariable Long planId, @RequestParam LocalDateTime newEndTime) {
        UUID memberId = getMemberId();
        return ResponseEntity.status(HttpStatus.OK).body(planService.updateEndTime(planId, newEndTime, memberId));
    }

    @PutMapping("/{planId}/vehicle")
    @Operation(summary = "일정 교통수단 변경", description = "일정의 교통수단을 변경합니다.")
    public ResponseEntity<PlanInfoDto> changeVehicle(@PathVariable Long planId, @RequestParam PlanVehicle newVehicle) {
        UUID memberId = getMemberId();
        return ResponseEntity.status(HttpStatus.OK).body(planService.updateVehicle(planId, newVehicle, memberId));
    }

    @PostMapping("/{planId}/finalize")
    @Operation(summary = "일정 확정후 구글캘린더 생성", description = "일정을 확정후 구글캘린더를 생성합니다.")
    public ResponseEntity<Void> finalizePlan(@PathVariable Long planId, @RequestParam UUID memberId) {
        planService.finalizePlan(planId, memberId);
        return ResponseEntity.ok().build();
    }

    private UUID getMemberId() {
        return UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }


}
