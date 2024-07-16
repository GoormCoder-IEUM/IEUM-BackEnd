package com.goormcoder.ieum.service;

import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.domain.MemberRole;
import com.goormcoder.ieum.dto.request.MemberCreateDto;
import com.goormcoder.ieum.dto.request.MemberUpdateDto;
import com.goormcoder.ieum.dto.request.PasswordUpdateDto;
import com.goormcoder.ieum.repository.MemberRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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

    public Member findByLoginIdAndPassword(String loginId, String password) {
        return memberRepository.findByLoginIdAndPassword(loginId, password)
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

    @Transactional
    public Member update(Member member, MemberUpdateDto updateDto) {
        member.setName(updateDto.name());
        member.setGender(updateDto.gender());
        member.setBirth(updateDto.birth());
        memberRepository.save(member);
        return member;
    }

    @Transactional
    public void updatePassword(Member member, PasswordUpdateDto updateDto) {
        if (!member.getPassword().equals(updateDto.previousPassword())) {
            throw new AccessDeniedException("비밀번호가 일치하지 않습니다.");
        }

        member.setPassword(updateDto.newPassword());
        memberRepository.save(member);
    }

    private void checkExistedLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new EntityExistsException();
        }
    }

}
