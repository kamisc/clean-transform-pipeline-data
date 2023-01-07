package com.sewerynkamil.helper;

import com.sewerynkamil.model.WeatherData;
import com.sewerynkamil.model.WeatherLocation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class WeatherHelper {
    private Map<LocalDate, Map<WeatherLocation, WeatherData>> weather = new HashMap<>();
    private double maxTemp;
    private double minTemp;

    public WeatherHelper(YearMonth ym) {
        try (FileInputStream fis = new FileInputStream("data/weather-" + ym + ".ser")) {
            ObjectInputStream oos = new ObjectInputStream(fis);
            weather = (Map<LocalDate, Map<WeatherLocation, WeatherData>>) oos.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        maxTemp = weather.values().stream()
                .flatMap(m -> m.values().stream())
                .map(WeatherData::getTemperature)
                .max(Double::compareTo)
                .orElse(75.0);

        maxTemp = weather.values().stream()
                .flatMap(m -> m.values().stream())
                .map(WeatherData::getTemperature)
                .min(Double::compareTo)
                .orElse(-75.0);
    }

    public WeatherData getWeather(LocalDate when, WeatherLocation targetLocation) {
        Map<WeatherLocation, WeatherData> dayWeather = weather.get(when);
        if (dayWeather == null) throw new IllegalArgumentException("Day weather not found");

        WeatherData d = dayWeather.get(targetLocation);
        if (d == null) throw new IllegalArgumentException("Target location not found");

        return d;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }
}
