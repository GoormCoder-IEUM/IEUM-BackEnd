package com.goormcoder.ieum.repository;

import com.goormcoder.ieum.domain.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    List<WeatherData> findByDestinationNameAndDateBetween(String destinationName, LocalDate startDate, LocalDate endDate);

    WeatherData findByDestinationNameAndDate(String destinationName, LocalDate date);
}

