package com.example.dell.moviesapplication.models;

import android.icu.util.Calendar;
/**
 * Created by Anca on 1/14/2018.
 */

public class Consult {

    private String cnp;
    private Calendar time;

    public Consult() {
    }

    public Consult( String patient_id, Calendar time) {

        this.cnp = patient_id;
        this.time = time;
    }

    public Consult(Calendar time){
        this.time = time;
    }


    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Consult{" +
                ", cnp='" + cnp + '\'' +
                ", time=" + time +
                '}';
    }
}
