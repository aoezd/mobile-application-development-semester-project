package com.aoezdemir.todoapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Switch;

import com.aoezdemir.todoapp.R;
import com.aoezdemir.todoapp.activity.adapter.ContactAdapter;
import com.aoezdemir.todoapp.crud.local.TodoDBHelper;
import com.aoezdemir.todoapp.crud.remote.ServiceFactory;
import com.aoezdemir.todoapp.model.Todo;
import com.aoezdemir.todoapp.utils.AlertDialogMaker;
import com.aoezdemir.todoapp.utils.ContactUtils;

import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {

    public final static String INTENT_KEY_TODO = "ADD_KEY_TODO";
    private final static String TAG = AddActivity.class.getSimpleName();

    private Todo todo;
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        todo = new Todo();
        todo.setExpiry(((CalendarView) findViewById(R.id.cvAddExpiryDate)).getDate());
        adapter = new ContactAdapter(todo, null, getContentResolver(), this);
        RecyclerView rvContacts = findViewById(R.id.rvAddContacts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvContacts.setLayoutManager(linearLayoutManager);
        rvContacts.setAdapter(adapter);

        ((CalendarView) findViewById(R.id.cvAddExpiryDate)).setOnDateChangeListener((@NonNull CalendarView view, int year, int month, int dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            todo.setExpiry(calendar.getTime().getTime());
        });
        findViewById(R.id.bAddTodo).setOnClickListener((View v) -> {
            // If no title was set -> show alert dialog
            String name = ((EditText) findViewById(R.id.etAddTitle)).getText().toString();
            if (name.isEmpty()) {
                AlertDialogMaker.makeNeutralOkAlertDialog(this, "No title set", "Please provide at least a title for the new todo.");
            } else {
                todo.setName(name);
                todo.setDescription(((EditText) findViewById(R.id.etAddDescription)).getText().toString());
                todo.setDone(false);
                todo.setFavourite(((Switch) findViewById(R.id.sAddFavourite)).isChecked());
                TodoDBHelper db = new TodoDBHelper(this);
                boolean dbInsertionSucceeded = db.insertTodo(todo);
                if (dbInsertionSucceeded) {
                    boolean isWebApiAccessible = getIntent().getBooleanExtra(RouterEmptyActivity.INTENT_IS_WEB_API_ACCESSIBLE, false);
                    if (isWebApiAccessible) {
                        ServiceFactory.getServiceTodo().deleteAllTodos().enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                Log.i(TAG, "All todos deleted on web API.");
                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                Log.e(TAG, "Could not deleteAllTodos all todos on web API." + t.getMessage());
                            }
                        });
                        for (Todo todo : db.getAllTodos()) {
                            ServiceFactory.getServiceTodo().createTodo(todo).enqueue(new Callback<Todo>() {
                                @Override
                                public void onResponse(@NonNull Call<Todo> call, @NonNull Response<Todo> response) {
                                    Log.i(TAG, "Todo with id '" + todo.getId() + "' was created on web API.");
                                }

                                @Override
                                public void onFailure(@NonNull Call<Todo> call, @NonNull Throwable t) {
                                    Log.e(TAG, "Could not deleteAllTodos all todos on web API." + t.getMessage());
                                }
                            });
                        }
                    }
                    Intent addTodoIntent = new Intent();
                    addTodoIntent.putExtra(INTENT_KEY_TODO, todo);
                    setResult(RESULT_OK, addTodoIntent);
                    finish();
                } else {
                    Log.d(TAG, "Failed to save into local database.");
                }
            }
        });
        findViewById(R.id.ibAddContact).setOnClickListener((View v) -> {
            if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(Manifest.permission.READ_CONTACTS)) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, EditActivity.REQUEST_PERMISSIONS);
                return;
            }
            startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), EditActivity.REQUEST_PICK_CONTACTS);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EditActivity.REQUEST_PICK_CONTACTS && resultCode == RESULT_OK && data != null && data.getData() != null) {
            todo.addContact(ContactUtils.getContactIdAndName(getContentResolver(), data.getData()));
            adapter.setContacts(todo.getContacts());
            adapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EditActivity.REQUEST_PERMISSIONS) {
            startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), EditActivity.REQUEST_PICK_CONTACTS);
        }
    }
}