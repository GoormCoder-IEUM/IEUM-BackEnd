package com.goormcoder.ieum.controller;

import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.dto.request.MemberUpdateDto;
import com.goormcoder.ieum.dto.request.PasswordUpdateDto;
import com.goormcoder.ieum.dto.response.MemberFindDto;
import com.goormcoder.ieum.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "My Page", description = "마이 페이지 관련 API")
@RequestMapping("/my-page")
public class MyPageController {

    private final MemberService memberService;

    @GetMapping
    @Operation(summary = "내 정보 조회")
    public ResponseEntity<MemberFindDto> findMemberInfo() {
        UUID memberId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Member member = memberService.findById(memberId);
        return ResponseEntity.ok(MemberFindDto.of(member));
    }

    @PostMapping
    @Operation(summary = "내 정보 수정", description = "이름, ")
    public ResponseEntity<MemberFindDto> updateInfo(@RequestBody MemberUpdateDto updateDto) {
        UUID memberId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Member member = memberService.findById(memberId);
        memberService.update(member, updateDto);
        return ResponseEntity.ok(MemberFindDto.of(member));
    }

    @PostMapping("/password")
    @Operation(summary = "비밀번호 수정")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateDto updateDto) {
        UUID memberId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Member member = memberService.findById(memberId);
        memberService.updatePassword(member, updateDto);
        return ResponseEntity.ok("비밀번호를 성공적으로 변경했습니다.");
    }

}
