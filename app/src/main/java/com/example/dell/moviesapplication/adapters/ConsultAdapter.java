package com.example.dell.moviesapplication.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.dell.moviesapplication.R;
import com.example.dell.moviesapplication.listeners.OnClickListenerPatientRecord;
import com.example.dell.moviesapplication.listeners.OnLongClickListenerAdminPatientRecord;
import com.example.dell.moviesapplication.models.Consult;
import com.example.dell.moviesapplication.models.Patient;

import java.util.ArrayList;

/**
 * Created by Anca on 1/14/2018.
 */

public class ConsultAdapter  extends ArrayAdapter<Consult> {

    private boolean isAdmin;
    public ConsultAdapter(Context context, ArrayList<Consult> consults){
        super(context,0,consults);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Consult consult = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_consult,parent,false);
        }

        DatePicker patientNameTextView= (DatePicker)convertView.findViewById(R.id.consultTime);
//        TextView patientEmailTextView= (TextView) convertView.findViewById(R.id.patientEmail);
//        TextView patientIdTextView= (TextView) convertView.findViewById(R.id.patientId);



        patientNameTextView.setMaxDate(consult != null ? Long.valueOf(String.valueOf(consult.getTime())) : null);
//        patientEmailTextView.setText("------>    " + patient.getEmail());
//        patientIdTextView.setText(patient.getId()+"");

        if(isAdmin){
            convertView.setOnLongClickListener(new OnLongClickListenerAdminPatientRecord());
        }else
        {
            convertView.setOnLongClickListener(new OnClickListenerPatientRecord());
        }


        return convertView;

    }
}
