package com.goormcoder.ieum.controller;

import com.goormcoder.ieum.domain.Chat;
import com.goormcoder.ieum.dto.request.ChatSendDto;
import com.goormcoder.ieum.dto.response.ChatFindDto;
import com.goormcoder.ieum.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Slf4j
@RestController
@Tag(name = "Chat", description = "채팅 관련 API")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/share-plan/chats")
    public void sendMessage(@Payload ChatSendDto chatSendDto) {
//        UUID memberId = TODO : 검증 후 획득 로직;
//        Chat message = chatService.saveMessage(chatSendDto, memberId);
        log.info("chatSendDto={}",chatSendDto);
        Chat message = chatService.saveMessage(chatSendDto);
        messagingTemplate.convertAndSend(
                "/sub/chats/plan/" + message.getPlan().getId(),
                ChatFindDto.of(message)
        );
    }

    @GetMapping("/chats/plans/{planId}")
    @Operation(summary = "메시지 조회", description = "특정 일정의 메시지를 조회합니다. (생성일 기준 오름차순 정렬)")
    public ResponseEntity<List<ChatFindDto>> getChatRoomMessages(@PathVariable Long planId) {
        UUID memberId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return ResponseEntity.status(HttpStatus.OK)
                .body(chatService.getChatMessages(planId, memberId));
    }

}
