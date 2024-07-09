package com.goormcoder.ieum.controller;

import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.dto.request.MemberCreateDto;
import com.goormcoder.ieum.dto.response.MemberFindDto;
import com.goormcoder.ieum.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Operation(summary = "자체 회원 가입")
    @PostMapping()
    public ResponseEntity<MemberFindDto> create(@RequestBody MemberCreateDto createDto) {
        Member member = memberService.createByLoginId(createDto);
        return ResponseEntity.ok(MemberFindDto.of(member));
    }

}
