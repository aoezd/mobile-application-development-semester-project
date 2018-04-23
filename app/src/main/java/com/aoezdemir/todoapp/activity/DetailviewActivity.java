package com.aoezdemir.todoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aoezdemir.todoapp.R;
import com.aoezdemir.todoapp.model.Todo;
import com.aoezdemir.todoapp.remote.ServiceFactory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailviewActivity extends AppCompatActivity {

    public final static String INTENT_KEY_TODO = "DETAIL_KEY_TODO";

    private Todo todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        todo = (Todo) getIntent().getSerializableExtra(INTENT_KEY_TODO);
        loadTodoTitle();
        loadTodoDescription();
        loadTodoDate();
        loadTodoDoneIcon();
        loadTodoFavouriteIcon();
        loadTodoEdit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.iDelete) {
            ServiceFactory.getServiceTodo().delete(todo.getId()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Todo was deleted.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error: Failed to delete todo.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        return true;
    }

    private void loadTodoTitle() {
        TextView tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailTitle.setText(todo.getName());
        tvDetailTitle.setTextColor(getResources().getColor(R.color.colorTodoTitleDefault, null));
    }

    private void loadTodoDescription() {
        TextView tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvDetailDescription.setText(todo.getDescription());
        tvDetailDescription.setTextColor(getResources().getColor(R.color.colorTodoDescriptionDefault, null));
    }

    private void loadTodoDate() {
        TextView tvDetailDate = findViewById(R.id.tvDetailDate);
        int textColor = todo.isDone() ? R.color.colorTodoDateDefault : todo.isExpired() ? R.color.colorTodoDateExpired : R.color.colorTodoDateDefault;
        tvDetailDate.setText(todo.formatExpiry());
        tvDetailDate.setTextColor(getResources().getColor(textColor, null));
        ((ImageView) findViewById(R.id.ivDetailDateIcon)).setImageDrawable(getResources().getDrawable(todo.isDone() ? R.drawable.ic_event_note_dark_gray_24dp : todo.isExpired() ? R.drawable.ic_event_note_red_24dp : R.drawable.ic_event_note_dark_gray_24dp, null));
    }

    private void loadTodoDoneIcon() {
        ImageView ibDetailDone = findViewById(R.id.ibDetailDone);
        ibDetailDone.setImageResource(todo.isDone() ? R.drawable.ic_check_circle_green_24dp : todo.isExpired() ? R.drawable.ic_error_outline_red_24dp : R.drawable.ic_radio_button_not_done_green_24dp);
    }

    private void loadTodoFavouriteIcon() {
        ImageView ibDetailFavourite = findViewById(R.id.ibDetailFavourite);
        ibDetailFavourite.setImageResource(todo.isFavourite() ? R.drawable.ic_favorite_red_24dp : R.drawable.ic_favorite_border_dark_gray_24dp);
    }

    private void loadTodoEdit() {
        android.support.design.widget.FloatingActionButton fbaEditTodo = findViewById(R.id.fbaEditTodo);
        fbaEditTodo.setOnClickListener((View v) -> {
            Intent editIntent = new Intent(v.getContext(), EditActivity.class);
            editIntent.putExtra(EditActivity.INTENT_KEY_TODO, todo);
            v.getContext().startActivity(editIntent);
        });
    }
}