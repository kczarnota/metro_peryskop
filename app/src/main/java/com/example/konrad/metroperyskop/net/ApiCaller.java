package com.example.konrad.metroperyskop.net;

import com.example.konrad.metroperyskop.utils.Constants;
import com.example.konrad.metroperyskop.model.ExitDetails;
import com.example.konrad.metroperyskop.model.Station;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCaller
{
    private static StationsService mStationService;

    static
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mStationService = retrofit.create(StationsService.class);
    }

    public static void makeExitApiCall(String stationId, String pointId, Callback<ExitDetails> callback)
    {
        Call<ExitDetails> exits = mStationService.getExit(stationId, pointId);
        exits.enqueue(callback);
    }

    public static void makeStationApiCall(String stationId, Callback<Station> callback)
    {
        Call<Station> stations = mStationService.getStation(stationId);
        stations.enqueue(callback);
    }

    public static void makeStationChangeApiCall(String stationId, String pointId, String state,
                                                Callback<Station> callback)
    {
        Call<Station> stations = mStationService.changeStationState(stationId, pointId, state);
        stations.enqueue(callback);
    }
}
