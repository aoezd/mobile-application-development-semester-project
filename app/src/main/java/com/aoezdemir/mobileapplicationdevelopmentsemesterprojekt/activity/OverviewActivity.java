package com.aoezdemir.mobileapplicationdevelopmentsemesterprojekt.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aoezdemir.mobileapplicationdevelopmentsemesterprojekt.R;
import com.aoezdemir.mobileapplicationdevelopmentsemesterprojekt.client.ClientTodo;
import com.aoezdemir.mobileapplicationdevelopmentsemesterprojekt.model.Todo;

import java.util.List;

public class OverviewActivity extends AppCompatActivity {

    private List<Todo> todos;
    private ClientTodo clientTodo;
    private RecyclerView rvOverview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clientTodo = new ClientTodo();
        todos = clientTodo.get();

        rvOverview = findViewById(R.id.rvOverview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvOverview.setLayoutManager(linearLayoutManager);
    }
}