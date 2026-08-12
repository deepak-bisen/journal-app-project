package com.ghtkdb.journal.application.service.impl;

import com.ghtkdb.journal.application.cache.AppCache;
import com.ghtkdb.journal.application.entity.WeatherResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {
    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final AppCache appCache;

    public WeatherResponse getWeather(String city) {
        log.info("Inside @class WeatherService in @method getWeather.");

        try {
            // Check if cache contains the API template
            String apiTemplate = appCache.appCache.get(AppCache.keys.Weather_API.toString());

            if (apiTemplate == null) {
                log.warn("Weather_API key not found in AppCache. Please check CONFIG_JOURNAL_APP table.");
                return null;
            }

            String finalAPI = apiTemplate
                    .replace("<city>", city)
                    .replace("<apiKey>", apiKey);

            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
            return response.getBody();

        } catch (Exception e) {
            log.error("Error fetching weather data: ", e);
            return null; // Return null gracefully so the user can still log in
        }
    }
}