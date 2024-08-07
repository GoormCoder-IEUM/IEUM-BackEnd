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
    @Operation(summary = "특정 지역의 현재 날씨 조회", description = "특정지역 입력시 현재 시간 기준으로 날씨가 반환됩니다")
    public ResponseEntity<WeatherData> getCurrentWeather(@PathVariable String destinationName) {
        try {
            WeatherData weatherData = weatherService.getCurrentWeather(destinationName);
            return ResponseEntity.status(HttpStatus.OK).body(weatherData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/fivedays/{destinationName}")
    @Operation(summary = "특정 지역의 5일 예보 조회", description = "특정지역 입력시 현재시간 기준으로 5일후 까지의 날씨가 3시간 단위로 반환됩니다.")
    public ResponseEntity<List<WeatherData>> getFiveDayForecast(@PathVariable String destinationName) {
        try {
            List<WeatherData> weatherDataList = weatherService.getFiveDaysWeather(destinationName);
            return ResponseEntity.status(HttpStatus.OK).body(weatherDataList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
