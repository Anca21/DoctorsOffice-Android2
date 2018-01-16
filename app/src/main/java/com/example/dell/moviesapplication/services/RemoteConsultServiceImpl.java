package com.example.dell.moviesapplication.services;

import com.example.dell.moviesapplication.BuildConfig;
import com.example.dell.moviesapplication.models.Consult;
import com.example.dell.moviesapplication.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Anca on 1/14/2018.
 */

public class RemoteConsultServiceImpl {

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.REMOTE_SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static RemoteConsultServiceImpl.RemoteConsultServiceInterface service = null;

    public static RemoteConsultServiceImpl.RemoteConsultServiceInterface getInstance() {
        if (service == null) {
            service = retrofit.create(RemoteConsultServiceImpl.RemoteConsultServiceInterface.class);
        }
        return service;
    }


    private static List<Observer> observers = new ArrayList<Observer>();
    public static void attach(Observer observer){
        observers.add(observer);
    }

    public static void notifyAllObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public interface RemoteConsultServiceInterface  {

        @PUT("/consult/{cnp}.json")
        Call<Consult> createConsult(
                @Path("cnp") String cnp,
                @Body Consult consult);

        @GET("/consult/{cnp}.json")
        Call<Consult> getConsult(@Path("cnp") String cnp);

        @GET("/review/.json")
        Call<Map<String, Consult>> getAllConsults();

    }
}
