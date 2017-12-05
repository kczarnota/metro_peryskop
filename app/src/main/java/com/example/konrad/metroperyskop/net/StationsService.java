package com.example.konrad.metroperyskop.net;

import com.example.konrad.metroperyskop.model.ExitDetails;
import com.example.konrad.metroperyskop.model.Station;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface StationsService
{
    @GET("api/station/{stationId}/point/{pointId}")
    Call<ExitDetails> getExit(@Path("stationId") String stationId, @Path("pointId") String pointId);

    @GET("api/station/{stationId}")
    Call<Station> getStation(@Path("stationId") String stationId);

    @GET("api/change/station/{stationId}/point/{pointId}/state/{state}")
    Call<Station> changeStationState(@Path("stationId") String stationId, @Path("pointId") String pointId,
                             @Path("state") String state);

}
