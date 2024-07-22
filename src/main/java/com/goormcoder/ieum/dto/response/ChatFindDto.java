package com.goormcoder.ieum.dto.response;

import com.goormcoder.ieum.domain.Chat;

import java.time.LocalDateTime;
import java.util.List;

public record ChatFindDto(

        Long messageId,
        String message,
        LocalDateTime createdAt,
        MemberSummaryDto sender

) {

    public static List<ChatFindDto> listOf(List<Chat> chats) {
        return chats.stream()
                .map(ChatFindDto::of)
                .toList();
    }

    public static ChatFindDto of(Chat chat) {
        if(chat == null) {
            return null;
        }

        return new ChatFindDto(
                chat.getId(),
                chat.getMessage(),
                chat.getCreatedAt(),
                MemberSummaryDto.of(chat.getMember())
        );
    }

}
