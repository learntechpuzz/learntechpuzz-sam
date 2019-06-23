package com.learntechpuzz.lambda.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class User {

    private String name;
    private String email;
    private String mobile;

    public User(String json) {
        Gson gson = new Gson();
        User request = gson.fromJson(json, User.class);
        this.name = request.getName();
        this.email = request.getEmail();
        this.mobile = request.getMobile();
    }

    public String toString() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}