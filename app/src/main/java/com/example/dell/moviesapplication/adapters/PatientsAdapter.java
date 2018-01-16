package com.example.dell.moviesapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dell.moviesapplication.R;
import com.example.dell.moviesapplication.listeners.OnClickListenerPatientRecord;
import com.example.dell.moviesapplication.listeners.OnLongClickListenerAdminPatientRecord;
import com.example.dell.moviesapplication.models.Patient;

import java.util.ArrayList;

/**
 * Created by Anca on 1/14/2018.
 */

public class PatientsAdapter  extends ArrayAdapter<Patient> {

    private boolean isAdmin;
    public PatientsAdapter(Context context, ArrayList<Patient> patients, boolean isAdmin){
        super(context,0,patients);
        this.isAdmin=isAdmin;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Patient patient = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_patient,parent,false);
        }

        TextView patientNameTextView= (TextView) convertView.findViewById(R.id.patientName);
        TextView patientEmailTextView= (TextView) convertView.findViewById(R.id.patientEmail);
        TextView patientIdTextView= (TextView) convertView.findViewById(R.id.patientId);



        patientNameTextView.setText(patient != null ? patient.getName() : null);
        patientEmailTextView.setText("------>    " + patient.getEmail());
        patientIdTextView.setText(patient.getId()+"");

        if(isAdmin){
            convertView.setOnLongClickListener(new OnLongClickListenerAdminPatientRecord());
        }else
        {
            convertView.setOnLongClickListener(new OnClickListenerPatientRecord());
        }


        return convertView;

    }
}
