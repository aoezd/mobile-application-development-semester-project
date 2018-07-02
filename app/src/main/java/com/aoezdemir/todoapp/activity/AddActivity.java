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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {

    public final static String INTENT_KEY_TODO = "ADD_KEY_TODO";
    private final static String TAG = AddActivity.class.getSimpleName();

    private Todo todo;
    private Calendar expiry;
    private ContactAdapter adapter;
    private EditText etAddTitle;
    private Button bAddTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        expiry = Calendar.getInstance(Locale.GERMAN);
        todo = new Todo();
        todo.setExpiry(expiry.getTimeInMillis());
        adapter = new ContactAdapter(todo, null, getContentResolver(), this);
        RecyclerView rvContacts = findViewById(R.id.rvAddContacts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvContacts.setLayoutManager(linearLayoutManager);
        rvContacts.setAdapter(adapter);

        loadExpiryCalendar();
        loadTodoTime();
        loadAddTitle();
        loadAddButton();
        loadAddContacts();
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

    private void loadAddTitle() {
        etAddTitle = findViewById(R.id.etAddTitle);
        etAddTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etAddTitle.getText().toString().trim().isEmpty()) {
                    enableAddButton();
                } else {
                    disableAddButton();
                }
            }
        });
    }

    private void loadAddButton() {
        bAddTodo = findViewById(R.id.bAddTodo);
        bAddTodo.setOnClickListener((View v) -> {
            // If no title was set -> show alert dialog
            String name = ((EditText) findViewById(R.id.etAddTitle)).getText().toString();
            if (name.isEmpty()) {
                AlertDialogMaker.makeNeutralOkAlertDialog(this, "No title set", "Please provide at least a title for the new todo.");
            } else {
                todo.setName(name);
                todo.setDescription(((EditText) findViewById(R.id.etAddDescription)).getText().toString());
                todo.setDone(false);
                todo.setFavourite(((Switch) findViewById(R.id.sAddFavourite)).isChecked());
                todo.setExpiry(expiry.getTimeInMillis());
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
    }

    private boolean isValidTime(String time) {
        if (time != null && !time.isEmpty() && time.contains(":") && time.length() <= 5 && !time.startsWith(":") && !time.endsWith(":")) {
            int hour = Integer.valueOf(time.split(":")[0]);
            int minute = Integer.valueOf(time.split(":")[1]);
            return hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59;
        }
        return false;
    }

    private void loadTodoTime() {
        EditText etAddTime = findViewById(R.id.etAddTime);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.GERMAN);
        etAddTime.setText(sdf.format(new Date(todo.getExpiry())));
        etAddTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String time = etAddTime.getText().toString().trim();
                if (isValidTime(time)) {
                    expiry.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time.split(":")[0]));
                    expiry.set(Calendar.MINUTE, Integer.valueOf(time.split(":")[1]));
                    expiry.set(Calendar.SECOND, 0);
                    expiry.set(Calendar.MILLISECOND, 0);
                    enableAddButton();
                } else {
                    disableAddButton();
                }
            }
        });
    }

    public void loadExpiryCalendar() {
        ((CalendarView) findViewById(R.id.cvAddExpiryDate)).setOnDateChangeListener((@NonNull CalendarView view, int year, int month, int dayOfMonth) -> {
            expiry.set(Calendar.YEAR, year);
            expiry.set(Calendar.MONTH, month);
            expiry.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            enableAddButton();
        });
    }

    private void loadAddContacts() {
        findViewById(R.id.ibAddContact).setOnClickListener((View v) -> {
            if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(Manifest.permission.READ_CONTACTS)) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, EditActivity.REQUEST_PERMISSIONS);
                return;
            }
            startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), EditActivity.REQUEST_PICK_CONTACTS);
        });
    }

    public void enableAddButton() {
        bAddTodo.setEnabled(true);
        bAddTodo.setBackgroundColor(getResources().getColor(R.color.colorAccent, null));
    }

    public void disableAddButton() {
        bAddTodo.setEnabled(false);
        bAddTodo.setBackgroundColor(getResources().getColor(R.color.colorTodoTitleDone, null));
    }
}