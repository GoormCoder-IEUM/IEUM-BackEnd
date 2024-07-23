package com.goormcoder.ieum.controller;

import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.dto.response.MemberFindAllDto;
import com.goormcoder.ieum.dto.response.MemberFindDto;
import com.goormcoder.ieum.jwt.JwtProvider;
import com.goormcoder.ieum.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Member", description = "회원 관련 API")
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 상세 조회")
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberFindDto> findById(@PathVariable UUID memberId) {
        Member member = memberService.findById(memberId);
        return ResponseEntity.ok(MemberFindDto.of(member));
    }

    @Operation(summary = "회원 검색", description = "검색 키워드로 회원을 검색합니다. (본인 제외)")
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<MemberFindAllDto>> getMember(@PathVariable String keyword) {
        UUID memberId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getAllMembersByLoginIdContainingAndIdIsNot(keyword, memberId));
    }

}
