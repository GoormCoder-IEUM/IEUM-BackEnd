package com.goormcoder.ieum.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Place extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime endedAt;

    @Column(name = "place_name", nullable = false)
    private String placeName;

    @Column(name = "place_location", nullable = false)
    private String address;

    private LocalDateTime activatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Builder
    private Place(Plan plan, Member member, LocalDateTime startedAt, LocalDateTime endedAt,
                  String placeName, String address, Category category) {
        this.plan = plan;
        this.member = member;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.placeName = placeName;
        this.address = address;
        this.category = category;
    }

    public static Place of(Plan plan, Member member, LocalDateTime startedAt, LocalDateTime endedAt,
                           String placeName, String address, Category category) {
        return Place.builder()
                .plan(plan)
                .member(member)
                .startedAt(startedAt)
                .endedAt(endedAt)
                .placeName(placeName)
                .address(address)
                .category(category)
                .build();
    }

    public void marksActivatedAt() {
        this.activatedAt = LocalDateTime.now();
    }

    public boolean isActivated() {
        return this.activatedAt == null;
    }

    public void marksStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public void marksEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public void resetVisitTimes() {
        this.startedAt = null;
        this.endedAt = null;
    }

}
