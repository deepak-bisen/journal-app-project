package com.ghtkdb.journal.application.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class WeatherResponse {

    private Current current;

    @Data
    public class Current {
        public int temperature;

        @JsonProperty("weather_descriptions")
        public ArrayList<String> weatherDescriptions;

        public int feelslike;
        public int visibility;
    }


}




