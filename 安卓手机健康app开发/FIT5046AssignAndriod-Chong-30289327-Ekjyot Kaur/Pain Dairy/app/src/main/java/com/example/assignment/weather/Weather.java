package com.example.assignment.weather;

import com.google.gson.annotations.SerializedName;

public class Weather {

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    //json -> gson
    @SerializedName("main")
    public String weather;
}
