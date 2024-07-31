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
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private static final String API_KEY = "c61293364315a17179a89213e2995218";
    private static final String WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=%s&cnt=30&appid=%s&units=metric";

    //현재날씨만
    private static final String CURRENT_WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";


    private final WeatherDataRepository weatherDataRepository;
    private final PlanRepository planRepository;

    // 임시 메소드 추가
    public void updateWeatherDataManually() {
        for (DestinationName destination : DestinationName.values()) {
            fetchAndStoreWeatherData(destination.getEnName());
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateWeatherData() {
        for (DestinationName destination : DestinationName.values()) {
            fetchAndStoreWeatherData(destination.getEnName());
        }
    }

    private void fetchAndStoreWeatherData(String destinationName) {
        String url = String.format(WEATHER_API_URL, destinationName, API_KEY);
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        if (response != null) {
            parseAndSaveWeatherData(response, destinationName);
        } else {
            System.err.println("Failed to fetch weather data for " + destinationName);
        }
    }

    private void parseAndSaveWeatherData(String response, String destinationName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response);
            JsonNode list = root.path("list");

            for (JsonNode dayNode : list) {
                long timestamp = dayNode.path("dt").asLong();
                LocalDate date = Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
                double temperature = dayNode.path("temp").path("day").asDouble();
                int humidity = dayNode.path("humidity").asInt();
                String weatherDescription = dayNode.path("weather").get(0).path("description").asText();

                WeatherData weatherData = WeatherData.builder()
                        .destinationName(destinationName)
                        .date(date)
                        .temperature(temperature)
                        .humidity(humidity)
                        .weatherDescription(weatherDescription)
                        .build();

                weatherDataRepository.save(weatherData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<WeatherData> getWeatherDataForMonth(String destinationName) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);
        return weatherDataRepository.findByDestinationNameAndDateBetween(destinationName, startDate, endDate);
    }

    public List<WeatherData> getWeatherDataForMonth(String destinationName, LocalDate startDate, LocalDate endDate) {
        return weatherDataRepository.findByDestinationNameAndDateBetween(destinationName, startDate, endDate);
    }

    public WeatherData getWeatherDataForDay(String destinationName, LocalDate date) {
        return weatherDataRepository.findByDestinationNameAndDate(destinationName, date);
    }

    public void addWeatherDataToPlan(Long planId, LocalDate startDate, LocalDate endDate) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        List<WeatherData> weatherDataList = weatherDataRepository.findByDestinationNameAndDateBetween(
                String.valueOf(plan.getDestination().getDestinationName()),
                startDate,
                endDate
        );

        for (WeatherData weatherData : weatherDataList) {
            weatherData.setPlan(plan);
            plan.addWeatherData(weatherData);
        }

        planRepository.save(plan);
    }

    // 현재 날씨를 가져오는 메서드 수정
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
            LocalDate today = LocalDate.now();

            // WeatherData 객체 생성
            return WeatherData.builder()
                    .destinationName(destinationName)
                    .date(today)
                    .temperature(temperature)
                    .humidity(humidity)
                    .weatherDescription(weatherDescription)
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse current weather data", e);
        }
    }
}
