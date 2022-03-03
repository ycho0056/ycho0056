package com.example.assignment.map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapRetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL="https://maps.googleapis.com/maps/api/geocode/";
    public static MapRetrofitInterface getRetrofitService(){
        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(MapRetrofitInterface.class);
    }
}
