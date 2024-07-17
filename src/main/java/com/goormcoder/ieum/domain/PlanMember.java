package com.goormcoder.ieum.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PlanMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private PlanMember(Plan plan, Member member) {
        this.plan = plan;
        this.member = member;
    }

    public static PlanMember of(Plan plan, Member member) {
        return PlanMember.builder()
                .plan(plan)
                .member(member)
                .build();
    }

}
