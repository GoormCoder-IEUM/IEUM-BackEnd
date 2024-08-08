package com.goormcoder.ieum.controller;

import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.domain.enumeration.InviteAcceptance;
import com.goormcoder.ieum.dto.request.PlanMemberCreateDto;
import com.goormcoder.ieum.dto.response.InviteFindAllDto;
import com.goormcoder.ieum.dto.response.InviteResultDto;
import com.goormcoder.ieum.security.CustomUserDetails;
import com.goormcoder.ieum.service.PlanMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/plan/members")
@Tag(name = "Plan Member", description = "일정 멤버 관련 API")
@RequiredArgsConstructor
public class PlanMemberController {

    private final PlanMemberService planMemberService;

    @PostMapping("/invite/{planId}")
    @Operation(summary = "멤버 초대", description = "일정에 멤버를 초대합니다.")
    public ResponseEntity<InviteResultDto> invitePlanMember(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long planId, @RequestBody PlanMemberCreateDto planMemberCreateDto) {
        Member member = userDetails.getMember();
        InviteResultDto inviteResult = planMemberService.invitePlanMember(member, planId, planMemberCreateDto.memberIds());
        return ResponseEntity.status(HttpStatus.OK).body(inviteResult);
    }

    @GetMapping("/invite/{planId}")
    @Operation(summary = "보낸 초대 조회", description = "보낸 초대를 조회합니다. (해당 일정의 모든 초대를 조회)")
    public ResponseEntity<List<InviteFindAllDto>> findAllInviteByPlanId(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long planId) {
        Member member = userDetails.getMember();
        return ResponseEntity.status(HttpStatus.OK).body(planMemberService.findAllInviteById(member, planId));
    }

    @GetMapping("/invite")
    @Operation(summary = "받은 초대 조회", description = "받은 초대를 조회합니다.")
    public ResponseEntity<List<InviteFindAllDto>> findAllInviteByMemberId(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Member member = userDetails.getMember();
        return ResponseEntity.status(HttpStatus.OK).body(planMemberService.findAllInviteById(member));
    }

    @PatchMapping("/invite/{planId}/{acceptance}")
    @Operation(summary = "멤버 초대 응답", description = "초대를 수락 또는 거절 합니다.")
    public ResponseEntity<String> responseToInvite(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long planId, @PathVariable InviteAcceptance acceptance) {
        Member member = userDetails.getMember();
        planMemberService.responseToInvite(member, planId, acceptance);
        return ResponseEntity.status(HttpStatus.OK).body("[" + acceptance + "] 응답이 완료되었습니다.");
    }

    @DeleteMapping("/invite/cancel/{planId}/{invitedMemberId}")
    @Operation(summary = "멤버 초대 취소", description = "보낸 초대를 취소합니다. (상대가 응답하기 전에만 가능)")
    public ResponseEntity<String> cancelInvite(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long planId, @PathVariable UUID invitedMemberId) {
        Member member = userDetails.getMember();
        planMemberService.cancelPlanMemberInvite(member, invitedMemberId, planId);
        return ResponseEntity.status(HttpStatus.OK).body("초대 취소가 완료되었습니다.");
    }


}
