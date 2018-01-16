package com.example.dell.moviesapplication.listeners;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.moviesapplication.R;

/**
 * Created by dell on 11/4/2017.
 */

public class OnClickListenerSendEmail implements View.OnClickListener {
    public void onClick(View view){
        final Context context = view.getRootView().getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.send_email, null, false);

        final EditText editTextEmail = (EditText) formElementsView.findViewById(R.id.editTextEmail);
        final EditText editTextEmailcontent = (EditText) formElementsView.findViewById(R.id.editTextEmailContent);

        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Send Email")
                .setPositiveButton("Send Email",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String email = editTextEmail.getText().toString();
                                String emailContent= editTextEmailcontent.getText().toString();
                                sendEmail(context, email,emailContent);
                                dialog.cancel();
                            }
                        }).show();
    }



    public void sendEmail(Context context,String email, String emailContent){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
        i.putExtra(Intent.EXTRA_SUBJECT, "Email sent from Android Studio");
        i.putExtra(Intent.EXTRA_TEXT   , emailContent);
        try {
            context.startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
