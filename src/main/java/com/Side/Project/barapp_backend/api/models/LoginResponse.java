package com.Side.Project.barapp_backend.api.models;

public class LoginResponse {


    private String jwt;
    private boolean succces;
    private String failureReason;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }


    public boolean isSuccces() {
        return succces;
    }

    public void setSuccces(boolean succces) {
        this.succces = succces;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}
