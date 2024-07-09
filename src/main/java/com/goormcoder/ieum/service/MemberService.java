package com.goormcoder.ieum.service;

import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findById(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

}
