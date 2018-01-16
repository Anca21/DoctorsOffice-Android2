package com.example.dell.moviesapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.dell.moviesapplication.listeners.OnClickListenerAddConsultation;
import com.example.dell.moviesapplication.models.Consult;
import com.example.dell.moviesapplication.models.Patient;
import com.example.dell.moviesapplication.services.RemoteConsultServiceImpl;
import com.example.dell.moviesapplication.services.RemoteDoctorsOfficeServiceImpl;
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

public class DetailsPatientActivity extends AppCompatActivity {

    private static final String TAG = "DetailsActivity";

    private RemoteDoctorsOfficeServiceImpl.RemoteDoctorsOfficeServiceInterface remotePatientService = RemoteDoctorsOfficeServiceImpl.getInstance();
    private RemoteConsultServiceImpl.RemoteConsultServiceInterface remoteConsultService = RemoteConsultServiceImpl.getInstance();
    private String globalCnp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_patient);

        Intent intent = getIntent();
        String patientName = intent.getStringExtra("patientName");

//        readConsults(globalCnp);
        setDetails(patientName);
//        Button buttonAddConsultation = (Button) findViewById(R.id.addConsultation);
//        buttonAddConsultation.setOnClickListener(new OnClickListenerAddConsultation(globalCnp, remotePatientService,remoteConsultService));
//
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

    public String setCnp(String cnp){

        return cnp;
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

    public void setDetails(String patientName){

        final TextView editPatientName = (TextView) findViewById(R.id.textPatientName);
        final TextView editPatientAddress = (TextView)findViewById(R.id.textPatientAddress);
        final TextView editPatientCnp = (TextView) findViewById(R.id.textPatientCnp);
        final TextView editPatientEmail = (TextView) findViewById(R.id.textPatientEmail);
        final TextView editPatientNextConsult = (TextView) findViewById(R.id.textPatientNextConsulta);

        Call<Patient> call = remotePatientService.getPatient(patientName);
        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(final Call<Patient> call,final Response<Patient> response) {
                final Patient patient = response.body();
                if (patient != null ) {
                    editPatientName.setText("Name: "+ patient.getName());
                    editPatientAddress.setText("Address: " + patient.getAddress());
                    editPatientCnp.setText("CNP: " + patient.getCnp());
                    editPatientEmail.setText("Email" + patient.getEmail());
                    editPatientNextConsult.setText("Next Consult: " + patient.getNext_consult());
                    setCnp(patient.getCnp());
                    Log.d(TAG, "~~~~~~~~~~~~~~~~~~~onResponse: PATIENT: " + patient.getName());
                } else {
                    Log.d(TAG, "~~~~~~~~~~~~~~~~~~~~~onResponse: No patient found with the NAME:" );
                }
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                Log.e(TAG, "~~~~~~~~~~~~~~~~`onResume: Failed to find patient..." + t.getLocalizedMessage(), t);

            }
        });
    }
//
////    public void readRecords() {
////        Call<Map<String, Patient>> call = remotePatientService.getAllPatients();
////        call.enqueue(new Callback<Map<String, Patient>>() {
////            @Override
////            public void onResponse(
////                    final Call<Map<String, Patient>> call,
////                    final Response<Map<String, Patient>> response) {
////                final Map<String, Patient> patientMap = response.body();
////                if (patientMap != null && !patientMap.isEmpty()) {
////                    displayData(patientMap);
////                    Log.d(TAG, "******************onResponse: Patients found as map with size: " + patientMap.size());
////                } else {
////                    Log.d(TAG, "******************onResponse: No patients found");
////                }
////            }
////
////            @Override
////            public void onFailure(
////                    final Call<Map<String, Patient>> call,
////                    final Throwable t) {
////                Log.e(TAG, "**********************onResume: Failed to find patients..." + t.getLocalizedMessage(), t);
////            }
////        });
////    }
}
