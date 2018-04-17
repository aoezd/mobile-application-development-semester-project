package com.aoezdemir.mobileapplicationdevelopmentsemesterprojekt.model;

public class User {

    private Long id;
    private String name;
    private String password;
    private String email;

    public User() {
        //  Jackson
    }

    public User(Long id, String name, String password, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof User && this.id.equals(((User) object).getId());
    }

    @Override
    public String toString() {
        return "User {id = " + id + ", name = " + name + ", password = " + password +
                ", email = " + email + "}";
    }
}
