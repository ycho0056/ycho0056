package com.example.assignment.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {
    //api.openweathermap.org/(base url)data/2.5/weather(API url)?q=Xi%27an,China&units=metric&appid=9ee7403fbc485ff18d5c16df61fe7233(parameter)
    @GET("weather")
    Call<WeatherReturn> weatherSearch(@Query("q") String City,
                                      @Query("units") String UNITS,
                                      @Query("appid") String APP_ID
                                      );
}
