package com.goormcoder.ieum.controller;

import com.goormcoder.ieum.domain.WeatherData;
import com.goormcoder.ieum.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/weather")
@Tag(name = "Weather", description = "날씨 관련 API")
@RequiredArgsConstructor
public class WeatherDataController {

    private final WeatherService weatherService;

    @GetMapping("/{destinationName}")
    @Operation(summary = "지역별 한 달 날씨 데이터 조회", description = "특정 지역의 한 달 동안의 날씨 데이터를 조회합니다.")
    public ResponseEntity<List<WeatherData>> getWeatherDataForMonth(@PathVariable String destinationName) {
        List<WeatherData> weatherData = weatherService.getWeatherDataForMonth(destinationName);
        return ResponseEntity.status(HttpStatus.OK).body(weatherData);
    }

    @GetMapping("/{destinationName}/{date}")
    @Operation(summary = "특정 날짜의 날씨 데이터 조회", description = "특정 지역의 특정 날짜 날씨 데이터를 조회합니다.")
    public ResponseEntity<WeatherData> getWeatherDataForDay(@PathVariable String destinationName, @PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        WeatherData weatherData = weatherService.getWeatherDataForDay(destinationName, localDate);
        return ResponseEntity.status(HttpStatus.OK).body(weatherData);
    }

    @PostMapping("/plan/{planId}")
    @Operation(summary = "계획에 날씨 데이터 추가", description = "특정 계획에 특정 날짜 범위의 날씨 데이터를 추가합니다.")
    public ResponseEntity<String> addWeatherDataToPlan(
            @PathVariable Long planId,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        weatherService.addWeatherDataToPlan(planId, start, end);
        return ResponseEntity.status(HttpStatus.OK).body("Weather data added to plan");
    }
}
