package com.aoezdemir.todoapp.model;

public class User {

    private String pwd;
    private String email;

    public User() {
    }

    public User(String email, String pwd) {
        this.email = email;
        this.pwd = pwd;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}