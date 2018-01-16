package com.example.dell.moviesapplication.listeners;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.dell.moviesapplication.DetailsPatientActivity;
import com.example.dell.moviesapplication.HomePatientActivity;
import com.example.dell.moviesapplication.R;
import com.example.dell.moviesapplication.models.Consult;
import com.example.dell.moviesapplication.services.RemoteConsultServiceImpl;
import com.example.dell.moviesapplication.services.RemoteDoctorsOfficeServiceImpl;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Anca on 1/14/2018.
 */

public class OnClickListenerPatientRecord implements View.OnLongClickListener{

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
        final CharSequence[] items = { "Review", "View Details"};

        new AlertDialog.Builder(context).setTitle("Patient Record")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            setTime(name);
                        } else if (item == 1) {
                            viewDetails(name, context);
                            ((HomePatientActivity) context).readRecords();

                        }
                        dialog.dismiss();
                    }
                }).show();
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setTime(final String name) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View setConsultView = inflater.inflate(R.layout.set_time, null, false);

        final DatePicker datePicker = (DatePicker) setConsultView.findViewById(R.id.date_picker);
        final TimePicker timePicker = (TimePicker) setConsultView.findViewById(R.id.time_picker);

        final Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth(),
                timePicker.getCurrentHour(),
                timePicker.getCurrentMinute());

//        time = calendar.getTimeInMillis();
        new AlertDialog.Builder(context)
                .setView(setConsultView)
                .setTitle("Consult")
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Consult consult =
                                        new Consult(
                                                name,
                                                calendar);
                                addConsult(consult);
                                ((HomePatientActivity) context).readRecords();

                                dialog.cancel();
                            }
                        }).show();
    }

    public void viewDetails(final String patientName, Context context) {
        Intent myIntent = new Intent(context, DetailsPatientActivity.class);
        myIntent.putExtra("patientName", patientName);
        context.startActivity(myIntent);
    }



    private void addConsult(Consult consult){
        Call<Consult> call = remoteConsultService.createConsult(consult.getCnp(), consult);
        call.enqueue(new Callback<Consult>() {
            @Override
            public void onResponse(
                    final Call<Consult> call,
                    final retrofit2.Response<Consult> response) {
                Log.d(TAG, "----------------------onResponse:CONSUUULT  merse");

            }

            @Override
            public void onFailure(
                    final Call<Consult> call,
                    final Throwable t) {
                Log.e(TAG, "----------------------onResponse: REVIEWWWW NUUUUU merse....." + t.getLocalizedMessage()  ,t);
            }
        });
    }

}
