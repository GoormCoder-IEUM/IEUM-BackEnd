package com.goormcoder.ieum.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Chat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message", nullable = false)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Builder
    private Chat(String message, Member member, Plan plan) {
        this.message = message;
        this.member = member;
        this.plan = plan;
    }

    public static Chat of(String message, Member member, Plan plan) {
        return Chat.builder()
                .message(message)
                .member(member)
                .plan(plan)
                .build();
    }

}
