package com.aoezdemir.mobileapplicationdevelopmentsemesterprojekt.model;

import java.util.Date;
import java.util.List;

public class Todo {

    private Long id;
    private String name;
    private String description;
    private Date expiry;
    private Boolean done;
    private Boolean favourite;
    private List<String> contacts;

    public Todo() {
        // Jackson
    }

    public Todo(Long id, String name, String description, Date expiry, Boolean done,
                Boolean favourite, List<String> contacts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.expiry = expiry;
        this.done = done;
        this.favourite = favourite;
        this.contacts = contacts;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public Boolean isDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Todo && this.id.equals(((Todo) object).getId());
    }

    @Override
    public String toString() {
        return "Todo {id = " + id + ", name = " + name + ", description = " + description +
                ", expiry = " + expiry + ", done = " + done + ", favourite = " + favourite +
                ", contacts = " + contacts + "}";
    }
}
