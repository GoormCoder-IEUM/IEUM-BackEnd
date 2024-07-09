package com.goormcoder.ieum.service;

import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.domain.MemberRole;
import com.goormcoder.ieum.dto.request.MemberCreateDto;
import com.goormcoder.ieum.repository.MemberRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findById(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Member createByLoginId(MemberCreateDto createDto) {
        checkExistedLoginId(createDto.loginId());
        Member member = Member.builder()
                .name(createDto.name())
                .role(MemberRole.USER)
                .gender(createDto.gender())
                .birth(createDto.birth())
                .loginId(createDto.loginId())
                .password(createDto.password())
                .oauthType(null)
                .oauthId(null)
                .build();
        memberRepository.save(member);
        return member;
    }

    private void checkExistedLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new EntityExistsException();
        }
    }

}
