package com.example.dell.moviesapplication.services;

import com.example.dell.moviesapplication.BuildConfig;
import com.example.dell.moviesapplication.models.Patient;
import com.example.dell.moviesapplication.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Anca on 1/14/2018.
 */

public class RemoteDoctorsOfficeServiceImpl {

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.REMOTE_SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // use gson converter
            .build();

    private static RemoteDoctorsOfficeServiceImpl.RemoteDoctorsOfficeServiceInterface service = null;

    public static RemoteDoctorsOfficeServiceImpl.RemoteDoctorsOfficeServiceInterface  getInstance() {
        if (service == null) {
            service = retrofit.create(RemoteDoctorsOfficeServiceImpl.RemoteDoctorsOfficeServiceInterface.class);
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

    public interface RemoteDoctorsOfficeServiceInterface  {

        @PUT("/patient/{name}.json")
        Call<Patient> createPatient(
                @Path("name") String name,
                @Body Patient patient);

        @GET("/patient/{name}.json")
        Call<Patient> getPatient(@Path("name") String name);

        @GET("/patient/.json")
        Call<Map<String, Patient>> getAllPatients();

        @DELETE("/patient/{name}.json")
        Call<Patient> deletePatient(@Path("name") String name);

    }
}
