package com.aoezdemir.todoapp.crud.remote;

import com.aoezdemir.todoapp.model.Todo;
import com.aoezdemir.todoapp.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceTodo {

    @GET("/api/todos/")
    Call<List<Todo>> readAllTodos();

    @GET("/api/todos/{id}")
    Call<Todo> readTodo(@Path("id") Long id);

    @PUT("/api/todos/{id}")
    Call<Todo> updateTodo(@Path("id") Long id, @Body Todo todo);

    @POST("/api/todos/")
    Call<Todo> createTodo(@Body Todo todo);

    @DELETE("/api/todos/")
    Call<ResponseBody> deleteAllTodos();

    @DELETE("/api/todos/{id}")
    Call<ResponseBody> deleteTodo(@Path("id") Long id);

    @PUT("/api/users/auth")
    Call<Boolean> authenticateUser(@Body User user);
}