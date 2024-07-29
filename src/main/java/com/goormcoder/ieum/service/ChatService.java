package com.goormcoder.ieum.service;

import com.goormcoder.ieum.domain.Chat;
import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.domain.Plan;
import com.goormcoder.ieum.dto.request.ChatSendDto;
import com.goormcoder.ieum.dto.response.ChatFindDto;
import com.goormcoder.ieum.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    private final MemberService memberService;
    private final PlanService planService;
    private final PlanMemberService planMemberService;

    @Transactional
    public Chat saveMessage(ChatSendDto chatSendDto, UUID memberId) {
        Plan plan = planService.findByPlanId(chatSendDto.planId());
        Member sender = memberService.findById(memberId);
        planMemberService.checkContainPlanMember(sender.getId(), plan.getId());

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
}
