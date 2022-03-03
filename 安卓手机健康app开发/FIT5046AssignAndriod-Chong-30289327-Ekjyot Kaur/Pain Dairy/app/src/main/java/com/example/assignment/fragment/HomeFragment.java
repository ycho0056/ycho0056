package com.example.assignment.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment.MainActivity;
import com.example.assignment.Signin;
import com.example.assignment.Signup;
import com.example.assignment.databinding.ActivityMainBinding;
import com.example.assignment.databinding.HomeFragmentBinding;
import com.example.assignment.roomdatabase.viewmodel.PainRecordViewModel;
import com.example.assignment.viewmodel.SharedViewModel;
import com.example.assignment.weather.Main;
import com.example.assignment.weather.RetrofitClient;
import com.example.assignment.weather.RetrofitInterface;
import com.example.assignment.weather.Weather;
import com.example.assignment.weather.WeatherReturn;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    private SharedViewModel model;
    private HomeFragmentBinding addBinding;
    private static final String APP_ID="9ee7403fbc485ff18d5c16df61fe7233";
    private static final String UNITS ="metric";
    private String City = "Xi'an,CN";
    private RetrofitInterface retrofitInterface;
    public static List<Weather> weatherList;
    public static Main mainWeather;
    public static PainRecordViewModel painRecordViewModel;
    public HomeFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        addBinding = HomeFragmentBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
        String todayTime = df.format(new Date());
        addBinding.date.setText("Date:   "+todayTime);
        retrofitInterface = RetrofitClient.getRetrofitService();
        Call<WeatherReturn> callAsync = retrofitInterface.weatherSearch(City,UNITS,APP_ID);
        callAsync.enqueue(new Callback<WeatherReturn>() {
            @Override
            public void onResponse(Call<WeatherReturn> call, Response<WeatherReturn> response) {
                if(response.isSuccessful())
                {
                    weatherList=response.body().weather;
                    String weather = weatherList.get(0).getWeather();
                    mainWeather = response.body().main;
                    addBinding.temp.setText("Temperature:  "+mainWeather.getTemp()+"°C");
                    addBinding.humidity.setText("Humidity:  "+ mainWeather.getHumidity());
                    addBinding.pressure.setText("Pressure:  "+mainWeather.getPressure());
                    addBinding.location.setText("Location:  "+City);
                    addBinding.weather.setText("Wather:  "+weather);
                }
                else{
                    Log.i("Error","Response Failled");
                }
            }

            @Override
            public void onFailure(Call<WeatherReturn> call, Throwable t) {
                Toast.makeText(view.getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        addBinding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getActivity(), Signin.class);
                startActivity(intent);
                Toast.makeText(view.getContext(),"Successfully log out!!!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }
    @Override
    public void onStart(){
        super.onStart();
        painRecordViewModel = new ViewModelProvider(this).get(PainRecordViewModel.class);
        painRecordViewModel.initail(getActivity().getApplication());
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addBinding = null;
    }

}