package com.blueconnectionz.nicenice.network.model;

import java.util.List;

import okhttp3.MultipartBody;

public class DriverRegisterReq {
    private String fullName;
    private String email;
    private String password;
    private String location;
    private List<MultipartBody.Part> documents;
    private String phoneNumber;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<MultipartBody.Part> getDocuments() {
        return documents;
    }

    public void setDocuments(List<MultipartBody.Part> documents) {
        this.documents = documents;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
