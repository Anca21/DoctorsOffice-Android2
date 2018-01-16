package com.example.dell.moviesapplication.listeners;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.dell.moviesapplication.HomePatientAdminActivity;
import com.example.dell.moviesapplication.R;
import com.example.dell.moviesapplication.models.Patient;
import com.example.dell.moviesapplication.services.RemoteDoctorsOfficeServiceImpl;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Anca on 1/14/2018.
 */

public class OnClickListenerAddPatient implements View.OnClickListener  {

    private RemoteDoctorsOfficeServiceImpl.RemoteDoctorsOfficeServiceInterface remoteService;
    private static final String TAG = "MyActivity";
    public OnClickListenerAddPatient(RemoteDoctorsOfficeServiceImpl.RemoteDoctorsOfficeServiceInterface service){
        remoteService = service;
    }

    public void onClick(View view){
        final Context context = view.getRootView().getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.add_patient, null, false);

        final EditText editTextPatientName = (EditText) formElementsView.findViewById(R.id.addTextPatientName);
        final EditText editTextPatientAddress = (EditText) formElementsView.findViewById(R.id.addTextPatientAddress);
        final EditText editTextPatientCNP= (EditText) formElementsView.findViewById(R.id.addTextPatientCNP);
        final EditText editTextPatientEmail = (EditText) formElementsView.findViewById(R.id.addTextPatientEmail);
        final EditText editTextPatientNextConsult = (EditText) formElementsView.findViewById(R.id.addTextPatientNextConsult);

        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Add Patient")
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String name = editTextPatientName.getText().toString();
                                String address = editTextPatientAddress.getText().toString();
                                String CNP =(editTextPatientCNP.getText().toString());
                                String email = editTextPatientEmail.getText().toString();
                                String next_consult = editTextPatientNextConsult.getText().toString();

                                Patient patient = new Patient(address,email,name,next_consult,CNP);
                                addPatient(patient);

                                //((MainActivity) context).readRecords();
                                ((HomePatientAdminActivity) context).readRecords();

                                dialog.cancel();
                            }
                        }).show();
    }



    private void addPatient(Patient patient){
        Call<Patient> call = remoteService.createPatient(patient.getName(), patient);
        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(
                    final Call<Patient> call,
                    final retrofit2.Response<Patient> response) {
                Log.d(TAG, "----------------------onResponse: merse");
                RemoteDoctorsOfficeServiceImpl.notifyAllObservers();
            }
            @Override
            public void onFailure(
                    final Call<Patient> call,
                    final Throwable t) {
                Log.e(TAG, "----------------------onResponse:NUUUUU merse....." + t.getLocalizedMessage()  ,t);
            }
        });
    }
}
