package com.example.generativeai.config;

import com.example.generativeai.dto.Weather;
import com.example.generativeai.functioncalling.WeatherService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class WeatherFunctionConfig {

    @Bean
    @Description("Get the weather of the city")
    Function<Weather.Request, Weather.Response> currentWeather() { return new WeatherService(); }
}
