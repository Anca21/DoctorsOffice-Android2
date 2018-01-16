package com.example.dell.moviesapplication.listeners;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dell.moviesapplication.DetailsPatientActivity;
import com.example.dell.moviesapplication.HomePatientAdminActivity;
import com.example.dell.moviesapplication.R;
import com.example.dell.moviesapplication.models.Patient;
import com.example.dell.moviesapplication.services.RemoteConsultServiceImpl;
import com.example.dell.moviesapplication.services.RemoteDoctorsOfficeServiceImpl;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anca on 1/14/2018.
 */

public class OnLongClickListenerAdminPatientRecord implements View.OnLongClickListener {

    private RemoteDoctorsOfficeServiceImpl.RemoteDoctorsOfficeServiceInterface remotePatientService = RemoteDoctorsOfficeServiceImpl.getInstance();
    private RemoteConsultServiceImpl.RemoteConsultServiceInterface remoteConsultService = RemoteConsultServiceImpl.getInstance();
    private static final String TAG = "MyActivity";
    Context context;
    String name;

    @Override
    public boolean onLongClick(View view) {

        context = view.getContext();
        TextView patientNameTextView = (TextView) view.findViewById(R.id.patientName);

        name = patientNameTextView.getText().toString().trim();
        final CharSequence[] items = {"Edit", "Delete","View Details"};

        new AlertDialog.Builder(context).setTitle("Patient Record")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            getPatient(name);
                        } else if (item == 1) {
                            deletePatient(name);
                            ((HomePatientAdminActivity) context).readRecords();

                        }
                        else if (item == 2) {
                            viewDetails(name, context);
                        }
                        dialog.dismiss();
                    }
                }).show();
        return false;
    }


    public void viewDetails(final String patientName, Context context) {
        Intent myIntent = new Intent(context, DetailsPatientActivity.class);
        myIntent.putExtra("patientName", patientName);
        context.startActivity(myIntent);
    }

    public void getPatient(final String name) {
        Call<Patient> call = remotePatientService.getPatient(name);
        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(final Call<Patient> call,final Response<Patient> response) {
                final Patient patient = response.body();
                if (patient != null ) {
                    editPatient(patient);
                    Log.d(TAG, "~~~~~~~~~~~~~~~~~~~onResponse: PATIENT: " + patient.getEmail());
                } else {
                    Log.d(TAG, "~~~~~~~~~~~~~~~~~~~~~onResponse: No patient found with the NAME:" + name);
                }
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                Log.e(TAG, "~~~~~~~~~~~~~~~~`onResume: Failed to find patient..." + t.getLocalizedMessage(), t);

            }
        });
    }



    public void editPatient(Patient patient) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.patient_input_form, null, false);

        final TextView textNamePatient = (TextView) formElementsView.findViewById(R.id.editTextPatientName);
        final EditText editAddressPatient = (EditText) formElementsView.findViewById(R.id.editTextPatientAddress);
        final EditText editCNPPatient = (EditText) formElementsView.findViewById(R.id.editCNPPatient);
        final EditText editEmailPatient = (EditText) formElementsView.findViewById(R.id.editEmailPatient);
        final EditText editNextConsultPatient = (EditText) formElementsView.findViewById(R.id.editNextConsultPatient);

        textNamePatient.setText(patient.getName());
        editAddressPatient.setText(patient.getAddress());
        editCNPPatient.setText(String.valueOf(patient.getCnp()));
        editEmailPatient.setText(patient.getEmail());
        editNextConsultPatient.setText(patient.getNext_consult());

        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Edit Record")
                .setPositiveButton("Save Changes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Patient patient = new Patient();
                                patient.setName(textNamePatient.getText().toString());
                                patient.setAddress(editAddressPatient.getText().toString());
                                patient.setCnp(editCNPPatient.getText().toString());
                                patient.setEmail(editEmailPatient.getText().toString());
                                patient.setNext_consult(editNextConsultPatient.getText().toString());

                                updatePatient(patient);
                                RemoteDoctorsOfficeServiceImpl.notifyAllObservers();
                                ((HomePatientAdminActivity) context).readRecords();


                                dialog.cancel();
                            }
                        }).show();
    }


    private void updatePatient(Patient patient) {
        Call<Patient> call = remotePatientService.createPatient(patient.getName(), patient);
        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(
                    final Call<Patient> call,
                    final Response<Patient> response) {
                //((MainActivity) context).readRecords();
                ((HomePatientAdminActivity) context).readRecords();

                Log.d(TAG, "----------------------onResponse: merse");
            }

            @Override
            public void onFailure(
                    final Call<Patient> call,
                    final Throwable t) {
                Log.e(TAG, "----------------------onResponse:NUUUUU merse....." + t.getLocalizedMessage(), t);
            }
        });
    }


    private void deletePatient(String name){
        Call<Patient> call = remotePatientService.deletePatient(name);
        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(
                    final Call<Patient> call,
                    final Response<Patient> response) {
                Log.d(TAG, "----------------------onResponse: merse");
                RemoteDoctorsOfficeServiceImpl.notifyAllObservers();
            }

            @Override
            public void onFailure(
                    final Call<Patient> call,
                    final Throwable t) {
                Log.e(TAG, "----------------------onResponse:NUUUUU merse....." + t.getLocalizedMessage(), t);
            }
        });
    }
}
