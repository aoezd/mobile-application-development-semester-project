package com.aoezdemir.todoapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aoezdemir.todoapp.R;

public class EditActivity extends AppCompatActivity {

    public final static String INTENT_KEY_TODO = "EDIT_KEY_TODO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
    }
}
