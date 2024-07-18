package com.goormcoder.ieum.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Invite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private InviteAcceptance acceptance;

    @Builder
    private Invite(Plan plan, Member member, InviteAcceptance acceptance) {
        this.plan = plan;
        this.member = member;
        this.acceptance = acceptance;
    }

    public static Invite of(Plan plan, Member member, InviteAcceptance acceptance) {
        return Invite.builder()
                .plan(plan)
                .member(member)
                .acceptance(acceptance)
                .build();
    }

}
