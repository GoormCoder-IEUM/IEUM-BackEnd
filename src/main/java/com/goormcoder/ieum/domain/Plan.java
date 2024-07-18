package com.goormcoder.ieum.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Plan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column(nullable = false)
    private LocalDateTime endedAt;

    @Column(nullable = false)
    private PlanVehicle vehicle;

    @OneToMany(mappedBy = "plan", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<PlanMember> planMembers = new ArrayList<>();

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Accommodation> accommodations;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Place> places;

    @Builder
    private Plan(String location, LocalDateTime startedAt, LocalDateTime endedAt, PlanVehicle vehicle) {
        this.location = location;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.vehicle = vehicle;
    }

    public static Plan of(String location, LocalDateTime startedAt, LocalDateTime endedAt, PlanVehicle vehicle) {
        return Plan.builder()
                .location(location)
                .startedAt(startedAt)
                .endedAt(endedAt)
                .vehicle(vehicle)
                .build();
    }

    public void addPlanMember(PlanMember planMember) {
        planMembers.add(planMember);
    }

}
