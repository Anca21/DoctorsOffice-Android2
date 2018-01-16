package com.example.dell.moviesapplication;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.dell.moviesapplication.adapters.PatientsAdapter;
import com.example.dell.moviesapplication.listeners.OnClickListenerAddPatient;
import com.example.dell.moviesapplication.listeners.OnClickListenerSendEmailPatient;
import com.example.dell.moviesapplication.models.Patient;
import com.example.dell.moviesapplication.observer.Observer;
import com.example.dell.moviesapplication.services.RemoteDoctorsOfficeServiceImpl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anca on 1/14/2018.
 */

public class HomePatientAdminActivity extends AppCompatActivity implements Observer {

    private RemoteDoctorsOfficeServiceImpl.RemoteDoctorsOfficeServiceInterface remoteService = RemoteDoctorsOfficeServiceImpl.getInstance();
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_patient);

        RemoteDoctorsOfficeServiceImpl.attach(this);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d(TAG, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa    Logged In USER: " + currentUser.getEmail());
        readRecords();

        Button buttonCreatePatient = (Button) findViewById(R.id.buttonAddPatient);
        buttonCreatePatient.setOnClickListener(new OnClickListenerAddPatient(remoteService));

        Button buttonSendEmail = (Button) findViewById(R.id.sendEmailButton);
        buttonSendEmail.setOnClickListener(new OnClickListenerSendEmailPatient());


        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomePatientAdminActivity.this, MainActivityPatients.class));
                finish();
            }
        });






    }


    @Override
    public void update() {
        Log.d(TAG, "OOOOOOBSEEEEEEEEEEEEEEEEEERVAAAAAAAABLE    ADMIN");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_mood_bad)
                        .setContentTitle("DoctorsOffice Application")
                        .setContentText( "The list of patients has been modified");


        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }



    public void displayData(Map<String, Patient> patientMap) {
        ArrayList<Patient> patientArrayList = new ArrayList<>();
        Log.d("PPPPP", "" +patientMap.size());
        for (Map.Entry<String, Patient> entry : patientMap.entrySet()) {
            patientArrayList.add(entry.getValue());
        }

        PatientsAdapter patientsAdapter = new PatientsAdapter(this, patientArrayList,true);
        ListView listView = (ListView) findViewById(R.id.patientListView);
        Log.d("PATIENT LIST: ", ""+(patientArrayList.size()));
        setPatientArrayListSize(patientArrayList.size());
        listView.setAdapter(patientsAdapter);
    }

    public int setPatientArrayListSize(int size){

        return size;
    }
    public void sendIntent(Map<String, Patient> patientMap){
        final ArrayList<Patient> patientArrayList = new ArrayList<>();
        Log.d("PPPPP", "" +patientMap.size());
        for (Map.Entry<String, Patient> entry : patientMap.entrySet()) {
            patientArrayList.add(entry.getValue());
        }

        final Button chartButton = (Button) findViewById(R.id.chartButton);
        chartButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intentChart = new Intent(getApplicationContext(), ChartActivity.class);
                    intentChart.putExtra("SIZE", patientArrayList.size());

                startActivity(intentChart);
            }
        });
    }
    public void readRecords() {
        Call<Map<String, Patient>> call = remoteService.getAllPatients();
        call.enqueue(new Callback<Map<String, Patient>>() {
            @Override
            public void onResponse(
                    final Call<Map<String, Patient>> call,
                    final Response<Map<String, Patient>> response) {
                final Map<String, Patient> patientMap = response.body();
                if (patientMap != null && !patientMap.isEmpty()) {
                    displayData(patientMap);
                    sendIntent(patientMap);
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
