package com.goormcoder.ieum.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class SocketPreHandler implements ChannelInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String accessToken = resolveToken(accessor);
        if (accessToken != null) {
            jwtProvider.getAuthentication(accessToken);
        }
        return message;
    }

    @EventListener(SessionConnectEvent.class)
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String accessToken = resolveToken(accessor);
        UUID memberId = getMemberId(accessToken);
        accessor.getSessionAttributes().put("memberId", memberId);
    }

    private String resolveToken(StompHeaderAccessor accessor) {
        log.info("Accessor={}", accessor);
        if (accessor.getCommand() == StompCommand.CONNECT) {
            String accessToken = accessor.getFirstNativeHeader("Authorization");
            String bearerToken = accessToken.trim();

            if (!bearerToken.trim().isEmpty() && bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }
        }

        return null;
    }

    private UUID getMemberId(String accessToken) {
            Authentication authentication = jwtProvider.getAuthentication(accessToken);
            UUID memberId = UUID.fromString(authentication.getPrincipal().toString());
            return memberId;
    }

}
