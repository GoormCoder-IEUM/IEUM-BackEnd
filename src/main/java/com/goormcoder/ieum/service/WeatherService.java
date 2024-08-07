package com.goormcoder.ieum.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goormcoder.ieum.domain.Plan;
import com.goormcoder.ieum.domain.WeatherData;
import com.goormcoder.ieum.domain.enumeration.DestinationName;
import com.goormcoder.ieum.repository.PlanRepository;
import com.goormcoder.ieum.repository.WeatherDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private static final String API_KEY = "c61293364315a17179a89213e2995218";
    private static final String FIVE_WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/forecast?q=%s&cnt=40&appid=%s&units=metric";
    private static final String CURRENT_WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";


    private final WeatherDataRepository weatherDataRepository;


    // 현재 날씨를 가져오는 메서드
    public WeatherData getCurrentWeather(String destinationName) {
        String url = String.format(CURRENT_WEATHER_API_URL, destinationName, API_KEY);
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        if (response != null) {
            return parseCurrentWeatherData(response, destinationName);
        } else {
            throw new RuntimeException("Failed to fetch current weather data for " + destinationName);
        }
    }

    private WeatherData parseCurrentWeatherData(String response, String destinationName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response);
            JsonNode main = root.path("main");
            JsonNode weather = root.path("weather").get(0);

            double temperature = main.path("temp").asDouble();
            int humidity = main.path("humidity").asInt();
            String weatherDescription = weather.path("description").asText();

            // 현재 날짜로 설정
            LocalDateTime now = LocalDateTime.now();

            // WeatherData 객체 생성
            return WeatherData.builder()
                    .destinationName(destinationName)
                    .dateTime(now)
                    .temperature(temperature)
                    .humidity(humidity)
                    .weatherDescription(weatherDescription)
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse current weather data", e);
        }
    }

    // 5일 예보를 가져오는 메서드
    public List<WeatherData> getFiveDaysWeather(String destinationName) {
        String url = String.format(FIVE_WEATHER_API_URL, destinationName, API_KEY);
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        if (response != null) {
            return parseFiveDaysWeatherData(response, destinationName);
        } else {
            throw new RuntimeException("Failed to fetch 5-day forecast data for " + destinationName);
        }
    }

    private List<WeatherData> parseFiveDaysWeatherData(String response, String destinationName) {
        ObjectMapper mapper = new ObjectMapper();
        List<WeatherData> weatherDataList = new ArrayList<>();

        try {
            JsonNode root = mapper.readTree(response);
            JsonNode list = root.path("list");

            for (JsonNode dayNode : list) {
                long timestamp = dayNode.path("dt").asLong();
                LocalDateTime dateTime = Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
                double temperature = dayNode.path("main").path("temp").asDouble();
                int humidity = dayNode.path("main").path("humidity").asInt();
                String weatherDescription = dayNode.path("weather").get(0).path("description").asText();

                WeatherData weatherData = WeatherData.builder()
                        .destinationName(destinationName)
                        .dateTime(dateTime)
                        .temperature(temperature)
                        .humidity(humidity)
                        .weatherDescription(weatherDescription)
                        .build();

                weatherDataList.add(weatherData);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse 5-day forecast data", e);
        }

        return weatherDataList;
    }

}
