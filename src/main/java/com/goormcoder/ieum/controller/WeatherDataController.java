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


    @GetMapping("/current/{destinationName}")
    @Operation(summary = "특정 지역의 현재 날씨 조회", description = "특정 지역의 현재 날씨를 조회합니다.")
    public ResponseEntity<WeatherData> getCurrentWeather(@PathVariable String destinationName) {
        try {
            WeatherData weatherData = weatherService.getCurrentWeather(destinationName);
            return ResponseEntity.status(HttpStatus.OK).body(weatherData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
