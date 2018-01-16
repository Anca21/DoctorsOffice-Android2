package com.example.dell.moviesapplication.listeners;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.dell.moviesapplication.DetailsPatientActivity;
import com.example.dell.moviesapplication.HomePatientActivity;
import com.example.dell.moviesapplication.R;
import com.example.dell.moviesapplication.models.Consult;
import com.example.dell.moviesapplication.models.Patient;
import com.example.dell.moviesapplication.services.RemoteConsultServiceImpl;
import com.example.dell.moviesapplication.services.RemoteDoctorsOfficeServiceImpl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anca on 1/14/2018.
 */

public class OnClickListenerAddConsultation implements View.OnClickListener  {

    private RemoteDoctorsOfficeServiceImpl.RemoteDoctorsOfficeServiceInterface remoteService;
    private RemoteConsultServiceImpl.RemoteConsultServiceInterface remoteConsultServiceInterface;
    private String getGlobalCnp;
    private static final String TAG = "MyActivity";
    public OnClickListenerAddConsultation(String globalCnp, RemoteDoctorsOfficeServiceImpl.RemoteDoctorsOfficeServiceInterface remoteS, RemoteConsultServiceImpl.RemoteConsultServiceInterface service){
        remoteConsultServiceInterface = service;
        remoteService = remoteS;
        getGlobalCnp = globalCnp;
    }

    public void onClick(View view){
        final Context context = view.getRootView().getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.add_consultation, null, false);

//        readRecords();
        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Add Consult")
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            public void onClick(DialogInterface dialog, int id) {
                                final DatePicker datePicker = (DatePicker) formElementsView.findViewById(R.id.date_picker);
                                final TimePicker timePicker = (TimePicker) formElementsView.findViewById(R.id.time_picker);

                                final Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                                        datePicker.getMonth(),
                                        datePicker.getDayOfMonth(),
                                        timePicker.getCurrentHour(),
                                        timePicker.getCurrentMinute());
                                Log.d("CNPPP", getGlobalCnp);
                                Consult consult = new Consult(getGlobalCnp,calendar);
                                Log.d("CONSULTTT", consult.toString());
                                addConsult(consult);

                                //((MainActivity) context).readRecords();
//                                ((DetailsPatientActivity) context).readRecords();

                                dialog.cancel();
                            }
                        }).show();
    }



    private void addConsult(Consult consult){
        Call<Consult> call = remoteConsultServiceInterface.createConsult(consult.getCnp(),consult );
//        Log.d("FUCKING", consult.getCnp());
        call.enqueue(new Callback<Consult>() {
            @Override
            public void onResponse(
                    final Call<Consult> call,
                    final retrofit2.Response<Consult> response) {
                Log.d(TAG, "----------------------onResponse: merse");
                RemoteConsultServiceImpl.notifyAllObservers();
            }
            @Override
            public void onFailure(
                    final Call<Consult> call,
                    final Throwable t) {
                Log.e(TAG, "----------------------onResponse:NUUUUU merse....." + t.getLocalizedMessage()  ,t);
            }
        });
    }

//    public void findPatientCnp(Map<String, Patient> patientMap){
//        Patient p = new Patient();
//        Log.d("CURRENT EMAIL", String.valueOf(this.currentUser));
//        for(Map.Entry<String, Patient> entry: patientMap.entrySet()){
//            if(entry.getValue().getEmail().equals(String.valueOf(this.currentUser.getEmail()))){
//                Log.d("PACIENT CU CNP", p.toString());
//                p = entry.getValue();
//            }
//        }
//
//        globalCnp = p.getCnp();
//        Log.d("THE FUCKING CNP", globalCnp);
//    }
    public void readRecords() {
        Call<Map<String, Patient>> call = remoteService.getAllPatients();
        call.enqueue(new Callback<Map<String, Patient>>() {
            @Override
            public void onResponse(
                    final Call<Map<String, Patient>> call,
                    final Response<Map<String, Patient>> response) {
                final Map<String, Patient> patientMap = response.body();
                if (patientMap != null && !patientMap.isEmpty()) {
//                    findPatientCnp(patientMap);
                    Log.d(TAG, "******************onResponse: Patients found as map with size: " + patientMap.size());
                } else {
                    Log.d(TAG, "******************onResponse: No patients found");
                }
            }

            @Override
            public void onFailure(
                    final Call<Map<String, Patient>> call,
                    final Throwable t) {
                Log.e(TAG, "**********************onResume: Failed to find patients..." + t.getLocalizedMessage(), t);
            }
        });
    }
}
