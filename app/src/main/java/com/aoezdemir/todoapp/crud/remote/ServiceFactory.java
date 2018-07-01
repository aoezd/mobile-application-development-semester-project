package com.aoezdemir.todoapp.crud.remote;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {

    //    private final static String BASE_URL = "http://192.168.178.23:8080/";
    public final static String BASE_URL = "http://192.168.178.27:8080/";

    public static ServiceTodo getServiceTodo() {
        return new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .connectTimeout(1, TimeUnit.SECONDS)
                        .build())
                .build()
                .create(ServiceTodo.class);
    }
}