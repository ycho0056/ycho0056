package com.example.assignment.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherReturn {

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    @SerializedName("weather")
    public List<Weather> weather;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    @SerializedName("main")
    public Main main;
}
