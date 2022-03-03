package com.example.assignment.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.assignment.databinding.MapsFragmentBinding;
import com.example.assignment.map.MapResponse;
import com.example.assignment.map.MapRetrofitClient;
import com.example.assignment.map.MapRetrofitInterface;
import com.example.assignment.viewmodel.SharedViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsFragment extends Fragment {
    private MapsFragmentBinding addBinding;
    private MapView mapView;
    private GoogleMap gMap;
    private String location;
    private Double longitude;
    private Double latitude;
    private static final String API_KEY = "AIzaSyBEQU2skJOL2vUYqS9vbxEKrAA3w9YA-U4";
    private MapRetrofitInterface mapRetrofitInterface;
    public MapsFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        addBinding = MapsFragmentBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();

        addBinding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = addBinding.locationEnter.getText().toString();
                mapRetrofitInterface = MapRetrofitClient.getRetrofitService();
                Call<MapResponse> callAsync = mapRetrofitInterface.placeSearch(location,API_KEY);
                callAsync.enqueue(new Callback<MapResponse>() {
                    @Override
                    public void onResponse(Call<MapResponse> call, Response<MapResponse> response) {
                        if(response.isSuccessful()){
                            latitude = response.body().results.get(0).geometry.location.lat;
                            longitude = response.body().results.get(0).geometry.location.lng;
                            mapView = (MapView) addBinding.map;
                            if(mapView!=null){
                                mapView.onCreate(null);
                                mapView.onResume();
                                mapView.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(@NonNull GoogleMap googleMap) {
                                        gMap = googleMap;
                                        LatLng location = new LatLng(latitude,longitude);
                                        gMap.addMarker(new MarkerOptions().position(location).title("Home location"));
                                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,10.f));

                                    }
                                });
                            }
                            else {
                                Toast.makeText(view.getContext(),"fail",Toast.LENGTH_SHORT).show();
                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<MapResponse> call, Throwable t) {

                    }
                });
            }
        });
        return view;

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addBinding = null;
    }
}
