package com.example.dell.moviesapplication.controller;

import com.example.dell.moviesapplication.models.Patient;
import com.example.dell.moviesapplication.services.RemoteDoctorsOfficeServiceImpl;

import java.util.Map;

/**
 * Created by Anca on 1/14/2018.
 */

public class PatientCtrl {

    private RemoteDoctorsOfficeServiceImpl.RemoteDoctorsOfficeServiceInterface remoteService=RemoteDoctorsOfficeServiceImpl.getInstance();
    private static final String TAG = "MyActivity";
    private Map<String, Patient> patientsToReturn;


    public PatientCtrl() {
    }
}
