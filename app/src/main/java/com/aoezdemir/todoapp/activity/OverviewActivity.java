package com.aoezdemir.todoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aoezdemir.todoapp.R;
import com.aoezdemir.todoapp.activity.adapter.OverviewAdapter;
import com.aoezdemir.todoapp.crud.local.TodoDBHelper;
import com.aoezdemir.todoapp.crud.remote.ServiceFactory;
import com.aoezdemir.todoapp.model.Todo;
import com.aoezdemir.todoapp.utils.TodoListSorter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OverviewActivity extends AppCompatActivity {

    private static final String TAG = OverviewActivity.class.getSimpleName();

    private RecyclerView rvOverview;
    private OverviewAdapter ovAdapter;
    private List<Todo> todos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvOverview = findViewById(R.id.rvOverview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvOverview.setLayoutManager(linearLayoutManager);
        ServiceFactory.getServiceTodo().get().enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                todos = response.body();
                if (todos != null) {
                    todos = TodoListSorter.sort(todos, true);
                    ovAdapter = new OverviewAdapter(todos);
                    rvOverview.setAdapter(ovAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Todo>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.fabAddTodo).setOnClickListener((View v) -> {
            startActivity(new Intent(this, AddActivity.class));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.iSortDate:
                todos = TodoListSorter.sort(todos, true);
                updateAdapter();
                return true;
            case R.id.iSortFavourite:
                todos = TodoListSorter.sort(todos, false);
                updateAdapter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OverviewAdapter.CREATE_NEW_TODO && resultCode == RESULT_OK && data != null) {
            todos.add((Todo) data.getSerializableExtra(AddActivity.INTENT_KEY_TODO));
            updateAdapter();
        }
    }

    private void updateAdapter() {
        ovAdapter.setTodos(todos);
        ovAdapter.notifyDataSetChanged();
    }
}