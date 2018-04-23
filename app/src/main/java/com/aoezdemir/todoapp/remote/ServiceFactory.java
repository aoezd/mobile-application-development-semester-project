package com.aoezdemir.todoapp.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {

    private final static String BASE_URL = "http://192.168.178.23:8080/";

    public static ServiceTodo getServiceTodo() {
        return new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ServiceTodo.class);
    }
}
