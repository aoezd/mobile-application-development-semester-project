package com.aoezdemir.todoapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.aoezdemir.todoapp.R;
import com.aoezdemir.todoapp.activity.adapter.OverviewAdapter;
import com.aoezdemir.todoapp.crud.ServiceFactory;
import com.aoezdemir.todoapp.model.Todo;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {

    public final static String INTENT_KEY_TODO = "EDIT_KEY_TODO";


    private Todo todo;
    private long expiry;
    private EditText etEditTitle;
    private EditText etEditDescription;
    private CalendarView cvEditExpiryDate;
    private Switch sEditDone;
    private Switch sEditFavourite;
    private Button bSaveTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        todo = (Todo) getIntent().getSerializableExtra(INTENT_KEY_TODO);
        loadSaveButton();
        loadTodoTitle();
        loadTodoExpiry();
        loadTodoDescription();
        loadTodoDone();
        loadTodoFavourite();
    }

    private void updateTodoWithUIData() {
        todo.setName(etEditTitle.getText().toString());
        todo.setExpiry(expiry);
        todo.setDone(sEditDone.isChecked());
        todo.setDescription(etEditDescription.getText().toString());
        todo.setFavourite(sEditFavourite.isChecked());
    }

    private void loadTodoTitle() {
        etEditTitle = findViewById(R.id.etEditTitle);
        etEditTitle.setText(todo.getName());
        etEditTitle.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            if (!hasFocus && !etEditTitle.getText().toString().equals(todo.getName())) {
                enableSaveButton();
            }
        });
    }

    private void loadTodoExpiry() {
        cvEditExpiryDate = findViewById(R.id.cvEditExpiryDate);
        expiry = todo.getExpiry();
        cvEditExpiryDate.setDate(expiry);
        cvEditExpiryDate.setOnDateChangeListener((@NonNull CalendarView view, int year, int month, int dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            expiry = calendar.getTime().getTime();
            enableSaveButton();
        });
    }

    private void loadTodoDescription() {
        etEditDescription = findViewById(R.id.etEditDescription);
        etEditDescription.setText(todo.getDescription());
        etEditDescription.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            if (!hasFocus && !etEditDescription.getText().toString().equals(todo.getDescription())) {
                enableSaveButton();
            }
        });
    }

    private void loadTodoDone() {
        sEditDone = findViewById(R.id.sEditDone);
        sEditDone.setChecked(todo.isDone());
        sEditDone.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            enableSaveButton();
        });
    }

    private void loadTodoFavourite() {
        sEditFavourite = findViewById(R.id.sEditFavourite);
        sEditFavourite.setChecked(todo.isFavourite());
        sEditFavourite.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            enableSaveButton();
        });
    }

    private void enableSaveButton() {
        bSaveTodo.setEnabled(true);
        bSaveTodo.setBackgroundColor(getResources().getColor(R.color.colorAccent, null));
    }

    private void loadSaveButton() {
        bSaveTodo = findViewById(R.id.bSaveTodo);
        bSaveTodo.setEnabled(false);
        bSaveTodo.setOnClickListener((View v) -> {
            updateTodoWithUIData();
            if (todo.getName().isEmpty()) {
                new AlertDialog.Builder(this).setTitle("No title set").setMessage("Please provide at least a title for the todo.").setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            } else {
                ServiceFactory.getServiceTodo().update(todo.getId(), todo).enqueue(new Callback<Todo>() {
                    @Override
                    public void onResponse(Call<Todo> call, Response<Todo> response) {
                        if (response.isSuccessful()) {
                            Intent editTodoIntent = new Intent();
                            editTodoIntent.putExtra(INTENT_KEY_TODO, todo);
                            setResult(OverviewAdapter.EDIT_TODO, editTodoIntent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to update Todo.", Toast.LENGTH_SHORT).show();
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
