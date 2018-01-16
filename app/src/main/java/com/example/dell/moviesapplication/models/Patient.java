package com.example.dell.moviesapplication.models;

/**
 * Created by Anca on 1/14/2018.
 */

public class Patient {

    private String id;
    private String address;
    private String email;
    private String name;
    private String next_consult;
    private String cnp;

    public Patient(String id, String address, String email, String name, String next_consult, String cnp) {
        this.id = id;
        this.address = address;
        this.email = email;
        this.name = name;
        this.next_consult = next_consult;
        this.cnp = cnp;
    }

    public Patient() {
    }

    public Patient(String address, String email, String name, String next_consult, String cnp) {
        this.address = address;
        this.email = email;
        this.name = name;
        this.next_consult = next_consult;
        this.cnp = cnp;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNext_consult() {
        return next_consult;
    }

    public void setNext_consult(String next_consult) {
        this.next_consult = next_consult;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", next_consult='" + next_consult + '\'' +
                ", cnp='" + cnp + '\'' +
                '}';
    }
}
