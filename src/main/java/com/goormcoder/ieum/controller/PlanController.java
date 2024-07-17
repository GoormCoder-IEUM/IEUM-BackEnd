package com.goormcoder.ieum.controller;


import com.goormcoder.ieum.dto.request.PlanCreateDto;
import com.goormcoder.ieum.service.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/plan")
@Tag(name = "Plan", description = "일정 관련 API")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @PostMapping
    @Operation(summary = "일정 생성", description = "일정을 생성합니다.")
    public ResponseEntity<String> createPlan(@RequestBody PlanCreateDto planCreateDto) {
        UUID memberId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        planService.createPlan(planCreateDto, memberId);
        return ResponseEntity.status(HttpStatus.OK).body("일정이 생성되었습니다.");
    }

}
