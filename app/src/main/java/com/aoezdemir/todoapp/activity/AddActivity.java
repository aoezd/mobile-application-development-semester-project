package com.aoezdemir.todoapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.aoezdemir.todoapp.R;
import com.aoezdemir.todoapp.activity.adapter.OverviewAdapter;
import com.aoezdemir.todoapp.model.Todo;
import com.aoezdemir.todoapp.crud.remote.ServiceFactory;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {

    public final static String INTENT_KEY_TODO = "ADD_KEY_TODO";

    private long expiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        expiry = ((CalendarView) findViewById(R.id.cvEditExpiryDate)).getDate();
        ((CalendarView) findViewById(R.id.cvEditExpiryDate)).setOnDateChangeListener((@NonNull CalendarView view, int year, int month, int dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            expiry = calendar.getTime().getTime();
        });
        findViewById(R.id.bAddTodo).setOnClickListener((View v) -> {
            // If no title was set -> show alert dialog
            String name = ((EditText) findViewById(R.id.etEditTitle)).getText().toString();
            if (name.isEmpty()) {
                new AlertDialog.Builder(this).setTitle("No title set").setMessage("Please provide at least a title for the new todo.").setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            } else {
                Todo todo = new Todo();
                todo.setName(name);
                todo.setDescription(((EditText) findViewById(R.id.etEditDescription)).getText().toString());
                todo.setDone(false);
                todo.setId(0L);
                todo.setFavourite(((Switch) findViewById(R.id.sEditFavourite)).isChecked());
                todo.setExpiry(expiry);
                ServiceFactory.getServiceTodo().create(todo).enqueue(new Callback<Todo>() {
                    @Override
                    public void onResponse(Call<Todo> call, Response<Todo> response) {
                        if (response.isSuccessful()) {
                            Intent addTodoIntent = new Intent();
                            addTodoIntent.putExtra(INTENT_KEY_TODO, todo);
                            setResult(OverviewAdapter.CREATE_NEW_TODO, addTodoIntent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to create new Todo.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Todo> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
