package com.goormcoder.ieum.service;

import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.domain.MemberRole;
import com.goormcoder.ieum.dto.request.MemberCreateDto;
import com.goormcoder.ieum.dto.request.MemberUpdateDto;
import com.goormcoder.ieum.dto.request.PasswordUpdateDto;
import com.goormcoder.ieum.dto.response.MemberFindAllDto;
import com.goormcoder.ieum.dto.response.OAuthUserInfoDto;
import com.goormcoder.ieum.repository.MemberRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member findById(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Member createOrFindByOAuthUserInfo(OAuthUserInfoDto oAuthUserInfoDto) {
        try {
            return this.findByOAuthUserInfo(oAuthUserInfoDto);
        }
        catch (EntityNotFoundException e) {
            return this.createByOAuthUserInfo(oAuthUserInfoDto);
        }
    }

    private Member createByOAuthUserInfo(OAuthUserInfoDto oAuthUserInfoDto) {
        Member member = Member.builder()
                .oauthType(oAuthUserInfoDto.registrationId())
                .oauthId(oAuthUserInfoDto.id())
                .name(oAuthUserInfoDto.name())
                .role(MemberRole.USER)
                .gender(oAuthUserInfoDto.gender())
                .birth(oAuthUserInfoDto.birth())
                .email(oAuthUserInfoDto.email())
                .loginId(null)
                .password(null)
                .build();
        memberRepository.save(member);
        log.info("created new member by oauth, id: {}", member.getId());
        return member;
    }

    private Member findByOAuthUserInfo(OAuthUserInfoDto oAuthUserInfoDto) {
        return memberRepository
                .findByOauthTypeAndOauthId(
                        oAuthUserInfoDto.registrationId(),
                        oAuthUserInfoDto.id()
                )
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<MemberFindAllDto> getAllMembersByLoginIdContainingAndIdIsNot(String keyword, Member member) {
        return MemberFindAllDto.listOf(memberRepository.findAllByLoginIdContainingAndIdIsNot(keyword, member.getId()));
    }

    public Member findByLoginIdAndPassword(String loginId, String password) {
        Member member = this.findByLoginId(loginId);
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new AccessDeniedException("비밀번호가 일치하지 않습니다.");
        }
        return member;
    }

    @Transactional
    public Member createByLoginId(MemberCreateDto createDto) {
        checkExistedLoginId(createDto.loginId());
        Member member = Member.builder()
                .name(createDto.name())
                .role(MemberRole.USER)
                .gender(createDto.gender())
                .birth(createDto.birth())
                .email(createDto.email())
                .loginId(createDto.loginId())
                .password(passwordEncoder.encode(createDto.password()))
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
        member.setEmail(updateDto.email());
        memberRepository.save(member);
        return member;
    }

    @Transactional
    public void updatePassword(Member member, PasswordUpdateDto updateDto) {
        if (!member.getPassword().equals(updateDto.previousPassword())) {
            throw new AccessDeniedException("비밀번호가 일치하지 않습니다.");
        }

        member.setPassword(passwordEncoder.encode(updateDto.newPassword()));
        memberRepository.save(member);
    }

    private void checkExistedLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new EntityExistsException();
        }
    }

}
