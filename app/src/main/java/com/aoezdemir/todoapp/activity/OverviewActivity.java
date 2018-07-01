package com.aoezdemir.todoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.aoezdemir.todoapp.R;
import com.aoezdemir.todoapp.activity.adapter.OverviewAdapter;
import com.aoezdemir.todoapp.crud.local.TodoDBHelper;
import com.aoezdemir.todoapp.crud.remote.ServiceFactory;
import com.aoezdemir.todoapp.model.Todo;
import com.aoezdemir.todoapp.utils.TodoListSorter;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OverviewActivity extends AppCompatActivity {

    private static final String TAG = OverviewActivity.class.getSimpleName();
    private RecyclerView rvOverview;
    private OverviewAdapter ovAdapter;
    private List<Todo> todos;
    private TodoDBHelper db;
    private boolean sortDateBased = true;
    private boolean isApiAccessable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvOverview = findViewById(R.id.rvOverview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvOverview.setLayoutManager(linearLayoutManager);

        if (getIntent().hasExtra(RouterEmptyActivity.INTENT_IS_WEB_API_ACCESSIBLE)) {
            isApiAccessable = getIntent().getBooleanExtra(RouterEmptyActivity.INTENT_IS_WEB_API_ACCESSIBLE, false);
        }
        db = new TodoDBHelper(this);
        todos = db.getAllTodos();
        if (isApiAccessable) {
            ServiceFactory.getServiceTodo().readAllTodos().enqueue(new Callback<List<Todo>>() {
                @Override
                public void onResponse(@NonNull Call<List<Todo>> call, @NonNull Response<List<Todo>> response) {
                    if (todos != null && !todos.isEmpty()) {
                        deleteAllApiTodos();
                        createAllApiTodosFromLocal();
                    } else {
                        todos = response.body();
                        db.deleteAllTodos();
                        db.insertAllTodos(todos);
                    }
                    initializeUIElements();
                }

                @Override
                public void onFailure(@NonNull Call<List<Todo>> call, @NonNull Throwable t) {
                    isApiAccessable = false;
                    initializeUIElements();
                }
            });
        } else {
            initializeUIElements();
        }
    }

    @Override
    public void onResume() {
        todos = db.getAllTodos();
        updateAdapter();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.iSort) {
            sortDateBased = item.getItemId() == R.id.iSortDate;
            updateAdapter();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OverviewAdapter.REQUEST_CREATE_NEW_TODO && resultCode == RESULT_OK &&
                data != null && data.hasExtra(AddActivity.INTENT_KEY_TODO)) {
            todos.add((Todo) data.getSerializableExtra(AddActivity.INTENT_KEY_TODO));
            updateAdapter();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Updates the todo list. In theory this method should be invoked every time the todo list had
     * been manipulated.
     */
    private void updateAdapter() {
        todos = TodoListSorter.sort(todos, sortDateBased);
        if (ovAdapter != null) {
            ovAdapter.setTodos(todos);
            ovAdapter.notifyDataSetChanged();
        }
    }

    private void deleteAllApiTodos() {
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
    }

    private void createAllApiTodosFromLocal() {
        for (Todo todo : todos) {
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

    private void initializeUIElements() {
        ovAdapter = new OverviewAdapter(todos, isApiAccessable);
        todos = TodoListSorter.sort(todos, sortDateBased);
        rvOverview.setAdapter(ovAdapter);
        findViewById(R.id.fabAddTodo).setOnClickListener((View v) -> {
            Intent intent = new Intent(this, AddActivity.class);
            intent.putExtra(RouterEmptyActivity.INTENT_IS_WEB_API_ACCESSIBLE, isApiAccessable);
            startActivityForResult(intent, OverviewAdapter.REQUEST_CREATE_NEW_TODO);
        });
    }
}