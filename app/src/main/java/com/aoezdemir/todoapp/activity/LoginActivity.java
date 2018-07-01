package com.aoezdemir.todoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aoezdemir.todoapp.R;
import com.aoezdemir.todoapp.crud.remote.ServiceFactory;
import com.aoezdemir.todoapp.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText etEmail;
    private EditText etPassword;
    private Button bLogin;
    private ProgressBar pbLogin;
    private TextView tvErrorInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.etLoginEmail);
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvErrorInfo.setVisibility(View.INVISIBLE);
                if (isValidEmailAddress()) {
                    etEmail.setError(null);
                    etEmail.setTextColor(getResources().getColor(R.color.colorTextDefault, null));
                    if (isValidPassword()) {
                        enableLoginButton();
                    }
                } else {
                    etEmail.setError("Please provide a valid email address");
                    etEmail.setTextColor(getResources().getColor(R.color.colorTextError, null));
                    disableLoginButton();
                }
            }
        });
        etPassword = findViewById(R.id.etLoginPassword);
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvErrorInfo.setVisibility(View.INVISIBLE);
                if (isValidPassword()) {
                    etPassword.setError(null);
                    etPassword.setTextColor(getResources().getColor(R.color.colorTextDefault, null));
                    if (isValidEmailAddress()) {
                        enableLoginButton();
                    }
                } else {
                    etPassword.setError("The password must provide exactly 6 numbers");
                    etPassword.setTextColor(getResources().getColor(R.color.colorTextError, null));
                    disableLoginButton();
                }
            }
        });
        tvErrorInfo = findViewById(R.id.tvErrorInfo);
        tvErrorInfo.setVisibility(View.INVISIBLE);
        pbLogin = findViewById(R.id.pbLogin);
        pbLogin.setVisibility(View.INVISIBLE);
        bLogin = findViewById(R.id.bLogin);
        disableLoginButton();
        bLogin.setOnClickListener((View v) -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            pbLogin.setVisibility(View.VISIBLE);
            ServiceFactory.getServiceTodo().authenticateUser(new User(email, password)).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                    // only to make the process bar longer visible
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.d(TAG, "Thread.sleep() was interrupted.");
                    }
                    pbLogin.setVisibility(View.INVISIBLE);
                    if (!response.isSuccessful()) {
                        Toast.makeText(v.getContext(), "Remote error: Failed to authenticate user (server error)", Toast.LENGTH_SHORT).show();
                    }
                    Boolean authSuccess = response.body();
                    if (authSuccess != null && authSuccess) {
                        Intent intent = new Intent(v.getContext(), OverviewActivity.class);
                        intent.putExtra(RouterEmptyActivity.INTENT_IS_WEB_API_ACCESSIBLE, true);
                        startActivity(intent);
                    } else {
                        tvErrorInfo.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                    pbLogin.setVisibility(View.INVISIBLE);
                    Toast.makeText(v.getContext(), "Remote error: Failed to authenticate user (client error)", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private boolean isValidEmailAddress() {
        String email = etEmail.getText().toString();
        return !email.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword() {
        String password = etPassword.getText().toString();
        return !password.isEmpty() && password.length() == 6;
    }

    private void enableLoginButton() {
        bLogin.setEnabled(true);
        bLogin.setBackgroundColor(getResources().getColor(R.color.colorAccent, null));
    }

    private void disableLoginButton() {
        bLogin.setEnabled(false);
        bLogin.setBackgroundColor(getResources().getColor(R.color.colorTodoTitleDone, null));
    }
}