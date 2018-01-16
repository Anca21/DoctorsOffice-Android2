package com.example.dell.moviesapplication;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dell.moviesapplication.listeners.OnClickListenerAddConsultation;
import com.example.dell.moviesapplication.models.Consult;
import com.example.dell.moviesapplication.models.Patient;
import com.example.dell.moviesapplication.observer.Observer;
import com.example.dell.moviesapplication.services.RemoteConsultServiceImpl;
import com.example.dell.moviesapplication.services.RemoteDoctorsOfficeServiceImpl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anca on 1/14/2018.
 */

public class HomePatientActivity extends AppCompatActivity implements Observer {

    private RemoteDoctorsOfficeServiceImpl.RemoteDoctorsOfficeServiceInterface remoteService = RemoteDoctorsOfficeServiceImpl.getInstance();
    private RemoteConsultServiceImpl.RemoteConsultServiceInterface remoteConsultService = RemoteConsultServiceImpl.getInstance();
    private static final String TAG = "MyActivity";
    FirebaseUser currentUser;

//    Map<String, Patient> globalPatients = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        this.currentUser = mAuth.getCurrentUser();
        if (this.currentUser != null) {
            Log.d(TAG, "ooooooooooooooooooooooooooo    Logged In USER: " + this.currentUser.getEmail());
        }

        RemoteDoctorsOfficeServiceImpl.attach(this);
//
//        Button buttonCreateConsult = (Button) findViewById(R.id.buttonAddConsultation);
//        buttonCreateConsult.setOnClickListener(new OnClickListenerAddConsultation(remoteService, remoteConsultService));

        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomePatientActivity.this, MainActivityPatients.class));
                finish();
            }
        });
        readRecords();

        Patient patient = null;
//        if (currentUser != null) {
//            patient = getPatientDetails(currentUser.getEmail());
//        }
//        setDetails(patient);

//        if (patient != null) {
//            readConsults(patient.getName());
//        }

    }

    private Patient getPatientDetails(Map<String, Patient> globalPatients) {
        Patient p = new Patient();
        Log.d("GLOBAL PATIENT", ""+globalPatients.size());
        Log.d("CURRENT EMAIL", String.valueOf(this.currentUser));
        for(Map.Entry<String, Patient> entry: globalPatients.entrySet()){
//            Log.d("THE EMAIL", entry.getValue().getEmail());
            if(entry.getValue().getEmail().equals(String.valueOf(this.currentUser.getEmail()))){
//                Log.d("IF IN",entry.getValue().toString());
                p = entry.getValue();
            }
        }
        setDetails(p);
        return p;
    }


    public void readRecords() {
        Call<Map<String, Patient>> call = remoteService.getAllPatients();
//        Log.d("remote", ""+call);
        call.enqueue(new Callback<Map<String, Patient>>() {

            @Override
            public void onResponse(
                    final Call<Map<String, Patient>> call,
                    final Response<Map<String, Patient>> response)
            {
//                Log.d("aiciiii", "");
                final Map<String, Patient> patiens = response.body();
                if (patiens != null && !patiens.isEmpty()) {
                    getPatientDetails(patiens);
//                    globalPatients = patiens;
                    Log.d(TAG, "******************onResponse: Patients found as map with size: " + patiens.size());
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

    public void readConsults(final String name) {
        Call<Map<String, Consult>> call = remoteConsultService.getAllConsults();
        call.enqueue(new Callback<Map<String, Consult>>() {
            @Override
            public void onResponse(
                    final Call<Map<String, Consult>> call,
                    final Response<Map<String, Consult>> response) {
                final Map<String, Consult> consults = response.body();
                if (consults != null && !consults.isEmpty()) {
                    createChart(name,consults);
                    Log.d(TAG, "******************onResponse: Consulta found as map with size: " + consults.size());
                } else {
                    Log.d(TAG, "******************onResponse: No consults found");
                }
            }

            @Override
            public void onFailure(
                    final Call<Map<String, Consult>> call,
                    final Throwable t) {
                Log.e(TAG, "**********************onResume: Failed to find consults..." + t.getLocalizedMessage(), t);
            }
        });
    }
    private void createChart(String patientName,Map<String, Consult> consults){
        GraphView graph = (GraphView) findViewById(R.id.graph);
        DataPoint[] points = new DataPoint[5];
        int number=0;

        Map<Integer,Integer> dict = createConsultDict(patientName,consults);

        for(Map.Entry<Integer, Integer> entry : dict.entrySet()) {

            int month = entry.getKey();
            int numberOfConsults = entry.getValue();
            points[number] = new DataPoint(month, numberOfConsults);
            number++;
        }
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(points);

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(50);

        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);

        graph.addSeries(series);

//        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
//            @Override
//            public String formatLabel(double value, boolean isValueX) {
//                if (isValueX) {
//                    return  Consult.()[(int)value].toString();
//                } else {
//                    return super.formatLabel(value, isValueX) ;
//                }
//            }
//        });
    }

    public Map<Integer,Integer> createConsultDict(String patientName,Map<String, Consult> consult){
        Map<Integer,Integer> dict= new HashMap<>();
        List<Consult> consultList = new ArrayList<>();
//        Map<Integer, String> moths= new HashMap<>();
//        moths.put(0, "Jan");moths.put(1,"Feb");moths.put(2,"Mar");moths.put(3,"Apr");moths.put(4,"May");
//        moths.put(5,"Jun");moths.put(6,"Jul");moths.put(7,"Aug");moths.put(8,"Sep");moths.put(9,"Oct");
//        moths.put(10,"Nov");moths.put(11,"Dec");

        for (Map.Entry<String, Consult> entry : consult.entrySet()) {
            if(entry.getValue().getCnp().equals(patientName)) {
                consultList.add(entry.getValue());
            }
        }
        for(Consult consult1 : consultList){
            int numbers = 0;
            for(int i=0; i<12;i++){
                if((consult1.getTime().MONTH)==i){
                    numbers++;
                }
            }
            dict.put(consult1.getTime().MONTH, numbers);
        }
//        Map<Integer, Integer> sortedDict = new TreeMap<>(dict);
        return dict;
    }

    @Override
    public void update() {
        Log.d(TAG, "OOOOOOBSEEEEEEEEEEEEEEEEEERVAAAAAAAABLE");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_mood_bad)
                        .setContentTitle("DocorsOffice Application")
                        .setContentText( "The list of patients has been modified");

        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public void setDetails(Patient patient){
        Log.d("PLM", patient.toString());
        TextView editPatientName = (TextView) findViewById(R.id.thePatientName);
        TextView editPatientAddress = (TextView)findViewById(R.id.thePatientAddress);
        TextView editPatientCnp = (TextView) findViewById(R.id.thePatientCnp);
        TextView editPatientEmail = (TextView) findViewById(R.id.thePatientEmail);
        TextView editPatientNextConsult = (TextView) findViewById(R.id.thePatientNextConsulta);

        editPatientName.setText(patient.getName());
        editPatientAddress.setText("Address: " + patient.getAddress());
        editPatientCnp.setText("CNP: " + patient.getCnp());
        editPatientEmail.setText("Email" + patient.getEmail());
        editPatientNextConsult.setText("Next Consult: " + patient.getNext_consult());

    }
}
