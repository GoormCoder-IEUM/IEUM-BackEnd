package com.goormcoder.ieum.service;

import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.repository.MemberRepository;
import com.goormcoder.ieum.security.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UUID id = UUID.fromString(username);
        Member member = memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return new CustomUserDetails(member);
    }

}