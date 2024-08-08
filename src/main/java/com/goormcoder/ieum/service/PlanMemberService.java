package com.goormcoder.ieum.service;

import com.goormcoder.ieum.domain.Invite;
import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.domain.Plan;
import com.goormcoder.ieum.domain.PlanMember;
import com.goormcoder.ieum.domain.enumeration.InviteAcceptance;
import com.goormcoder.ieum.dto.response.InviteFindAllDto;
import com.goormcoder.ieum.dto.response.InviteResultDto;
import com.goormcoder.ieum.exception.ConflictException;
import com.goormcoder.ieum.exception.ErrorMessages;
import com.goormcoder.ieum.repository.InviteRepository;
import com.goormcoder.ieum.repository.PlanMemberRepository;
import com.goormcoder.ieum.repository.PlanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanMemberService {

    private final InviteRepository inviteRepository;
    private final PlanRepository planRepository;
    private final PlanMemberRepository planMemberRepository;

    private final MemberService memberService;


    @Transactional
    public InviteResultDto invitePlanMember(Member member, Long planId, UUID[] memberIds) {
        // TODO : 일정에 일정 이름 필드 추가검토 -> 초대목록에 이름, 여행지로 표시되게 (ex : 놀러가자(제주))
        checkContainPlanMember(member.getId(), planId);
        Plan plan = findPlanByPlanId(planId);

        List<Member> inviteMembers = Stream.of(memberIds)
                .map(memberService::findById)
                .toList();

        List<Member> successMembers = new ArrayList<>();
        List<Member> duplicateMembers = new ArrayList<>();
        
        for(Member inviteMember : inviteMembers) {
            if(checkDuplicateInvite(inviteMember, planId)){
                duplicateMembers.add(inviteMember);
                continue; 
            }
            Invite invite = Invite.builder()
                    .member(inviteMember)
                    .plan(plan)
                    .acceptance(null)
                    .build();
            inviteRepository.save(invite);
            successMembers.add(inviteMember);
        };

        return InviteResultDto.of(successMembers, duplicateMembers);
    }

    @Transactional
    public void responseToInvite(Member member, Long planId, InviteAcceptance inviteAcceptance) {
        Invite invite = checkValidInvite(member.getId(), planId);

        invite.setAcceptance(inviteAcceptance);

        if (InviteAcceptance.ACCEPT.equals(inviteAcceptance)){
            addPlanMember(invite);
        }
    }

    @Transactional
    public void cancelPlanMemberInvite(Member member, UUID invitedMemberId, Long planId) {
        checkContainPlanMember(member.getId(), planId);
        Member invitedMember = memberService.findById(invitedMemberId);
        Invite invite = checkValidInvite(invitedMember.getId(), planId);

        invite.markAsDeleted();

    }

    public List<InviteFindAllDto> findAllInviteById(Member member){
        List<Invite> invites = inviteRepository.findAllByMemberId(member.getId());
        return InviteFindAllDto.listOf(invites);
    }

    public List<InviteFindAllDto> findAllInviteById(Member member, Long planId){
        checkContainPlanMember(member.getId(), planId);
        List<Invite> invites = inviteRepository.findAllByPlanId(planId);
        return InviteFindAllDto.listOf(invites);
    }




    private void addPlanMember(Invite invite) {
        Member member = invite.getMember();
        Plan plan = invite.getPlan();

        plan.addPlanMember(PlanMember.of(plan, member));

        planRepository.save(plan);
    }

    private Plan findPlanByPlanId(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MEMBER_NOT_FOUND.getMessage()));
    }

    private Invite findInviteByMemberIdAndPlanId(UUID memberId, Long planId) {
        return inviteRepository.findByMemberIdAndPlanId(memberId, planId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.INVITE_NOT_FOUND.getMessage()));
    }

    private boolean checkDuplicateInvite(Member member, Long planId) {
        Optional<Invite> optionalInvite = inviteRepository.findByMemberIdAndPlanIdAndIsNull(member.getId(), planId);
        if (optionalInvite.isEmpty()) {
            return false;
        }

        Invite invite = optionalInvite.get();
        return invite.getDeletedAt() == null && invite.getAcceptance() == null;
    }

    private Invite checkValidInvite(UUID memberId, Long planId) {
        Invite invite = findInviteByMemberIdAndPlanId(memberId, planId);

        if (invite.getDeletedAt() != null || invite.getAcceptance() != null)
            throw new ConflictException(ErrorMessages.INVITE_RESPONSE_CONFLICT);

        return invite;
    }

    public void checkContainPlanMember(UUID memberId, Long planId) {
        planMemberRepository.findByMemberIdAndPlanIdOrThrow(memberId, planId);
    }

}
