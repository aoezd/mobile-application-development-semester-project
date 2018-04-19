package com.aoezdemir.todoapp.remote;

import com.aoezdemir.todoapp.model.Todo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceTodo {

    @GET("/api/todos/")
    Call<List<Todo>> get();

    @GET("/api/todos/{id}")
    Call<Todo> get(@Path("id") Long id);

    @PUT("/api/todos/{id}")
    void update(@Path("id") Long id, @Body Todo todo);

    @POST("/api/todos/")
    void create(@Body Todo todo);

    @DELETE("/api/todos/")
    void delete();

    @DELETE("/api/todos/{id}")
    void delete(@Path("id") Long id);
}
