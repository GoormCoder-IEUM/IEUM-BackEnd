package com.goormcoder.ieum.service;

import com.goormcoder.ieum.domain.Chat;
import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.domain.Plan;
import com.goormcoder.ieum.dto.request.ChatSendDto;
import com.goormcoder.ieum.dto.response.ChatFindDto;
import com.goormcoder.ieum.exception.ErrorMessages;
import com.goormcoder.ieum.exception.PlaceShareWebSocketException;
import com.goormcoder.ieum.repository.ChatRepository;
import com.goormcoder.ieum.repository.MemberRepository;
import com.goormcoder.ieum.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;

    private final MemberService memberService;
    private final PlanService planService;
    private final PlanMemberService planMemberService;

    @Transactional
    public Chat saveMessage(ChatSendDto chatSendDto, Member member) {
        Member sender = memberRepository.findById(member.getId())
                .orElseThrow(() -> new PlaceShareWebSocketException(ErrorMessages.MEMBER_NOT_FOUND, null, null));
        Plan plan = planRepository.findById(chatSendDto.planId())
                .orElseThrow(() -> new PlaceShareWebSocketException(ErrorMessages.PLAN_NOT_FOUND, sender, null));

        log.info("chatSendDto={}", chatSendDto);

        validatePlanForWebSocket(plan, sender);

        Chat chat = Chat.of(chatSendDto.message(), sender, plan);
        plan.addMessage(chat);

        return chatRepository.save(chat);
    }

    public List<ChatFindDto> getChatMessages(Long planId, UUID memberId){
        Plan plan = planService.findByPlanId(planId);
        planMemberService.checkContainPlanMember(memberId, plan.getId());

        List<Chat> messages = chatRepository.findAllByPlanId(planId);
        return ChatFindDto.listOf(messages);
    }

    private static void validatePlanForWebSocket(Plan plan, Member sender) {
        plan.getPlanMembers().stream()
                .filter(planMember -> planMember.getMember().equals(sender))
                .findFirst()
                .orElseThrow(() -> new PlaceShareWebSocketException(ErrorMessages.PLAN_MEMBER_NOT_FOUND, sender, plan));
    }
}
