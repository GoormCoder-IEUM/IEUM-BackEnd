package com.goormcoder.ieum.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String destinationName;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private double temperature;

    @Column(nullable = false)
    private int humidity;

    @Column(nullable = false)
    private String weatherDescription;

    @Builder
    public WeatherData(String destinationName, LocalDate date, double temperature, int humidity, String weatherDescription) {
        this.destinationName = destinationName;
        this.date = date;
        this.temperature = temperature;
        this.humidity = humidity;
        this.weatherDescription = weatherDescription;
    }

}
