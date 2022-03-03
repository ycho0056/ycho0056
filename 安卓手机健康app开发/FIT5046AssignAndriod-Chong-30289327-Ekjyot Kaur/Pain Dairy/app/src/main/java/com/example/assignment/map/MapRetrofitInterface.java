package com.example.assignment.map;

import com.example.assignment.weather.WeatherReturn;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapRetrofitInterface {
    @GET("json")
    Call<MapResponse> placeSearch(@Query("address") String address,
                                      @Query("key") String API_KEY
                                      );
}
