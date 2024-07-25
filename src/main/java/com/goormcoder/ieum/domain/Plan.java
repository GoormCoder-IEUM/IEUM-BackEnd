package com.goormcoder.ieum.domain;

import com.goormcoder.ieum.domain.enumeration.PlanVehicle;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Plan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false)
    private Destination destination;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column(nullable = false)
    private LocalDateTime endedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlanVehicle vehicle;

    @OneToMany(mappedBy = "plan", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<PlanMember> planMembers = new ArrayList<>();

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Accommodation> accommodations;

    @OneToMany(mappedBy = "plan", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Place> places;

    @OneToMany(mappedBy = "plan", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Chat> messages = new ArrayList<>();

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<WeatherData> weatherData = new ArrayList<>();

    @Builder
    private Plan(Destination destination, LocalDateTime startedAt, LocalDateTime endedAt, PlanVehicle vehicle) {
        this.destination = destination;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.vehicle = vehicle;
    }

    public static Plan of(Destination destination, LocalDateTime startedAt, LocalDateTime endedAt, PlanVehicle vehicle) {
        return Plan.builder()
                .destination(destination)
                .startedAt(startedAt)
                .endedAt(endedAt)
                .vehicle(vehicle)
                .build();
    }

    public void addPlanMember(PlanMember planMember) {
        planMembers.add(planMember);
    }

    public void addPlace(Place place) {
        places.add(place);
    }

    public void addMessage(Chat chat) { messages.add(chat); }

    public void addWeatherData(WeatherData weatherData) { this.weatherData.add(weatherData); }

    public long getDuration() {
        return ChronoUnit.DAYS.between(startedAt.toLocalDate(), endedAt.toLocalDate()) + 1;
    }

    public LocalDate getNthDayDate(long day) {
        return this.startedAt.toLocalDate().plusDays(day - 1);
    }

}
