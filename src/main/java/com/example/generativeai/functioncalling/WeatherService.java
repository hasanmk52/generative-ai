package com.example.generativeai.functioncalling;

import com.example.generativeai.dto.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class WeatherService implements Function<Weather.Request, Weather.Response > {

    @Autowired
    private WeatherServiceBuilder weatherServiceBuilder;

    @Override
    public Weather.Response apply(Weather.Request request) {
        return weatherServiceBuilder.getWeather(request.city());
    }
}
