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
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private static final String API_KEY = "YOUR_API_KEY";
    private static final String WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=%s&cnt=30&appid=%s&units=metric";

    private final WeatherDataRepository weatherDataRepository;
    private final PlanRepository planRepository;

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

        parseAndSaveWeatherData(response, destinationName);
    }

    private void parseAndSaveWeatherData(String response, String destinationName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response);
            JsonNode list = root.path("list");

            for (JsonNode dayNode : list) {
                LocalDate date = LocalDate.ofEpochDay(dayNode.path("dt").asLong() / 86400);
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

    //한달 날씨 전체
    public List<WeatherData> getWeatherDataForMonth(String destinationName) {
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now();
        return weatherDataRepository.findByDestinationNameAndDateBetween(destinationName, startDate, endDate);
    }

    //일정 기간의 날씨
    public List<WeatherData> getWeatherDataForMonth(String destinationName, LocalDate startDate, LocalDate endDate) {
        return weatherDataRepository.findByDestinationNameAndDateBetween(destinationName, startDate, endDate);
    }

    //특정 날짜 날씨
    public WeatherData getWeatherDataForDay(String destinationName, LocalDate date) {
        return weatherDataRepository.findByDestinationNameAndDate(destinationName, date);
    }

    public void addWeatherDataToPlan(Long planId, LocalDate startDate, LocalDate endDate) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        List<WeatherData> weatherDataList = weatherDataRepository.findByDestinationNameAndDateBetween(
                String.valueOf(plan.getDestination()),
                startDate,
                endDate
        );

        for (WeatherData weatherData : weatherDataList) {
            weatherData.setPlan(plan);
            plan.addWeatherData(weatherData);
        }

        planRepository.save(plan);
    }
}
