package com.aoezdemir.todoapp.activity.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aoezdemir.todoapp.R;
import com.aoezdemir.todoapp.activity.DetailviewActivity;
import com.aoezdemir.todoapp.activity.EditActivity;
import com.aoezdemir.todoapp.model.Todo;
import com.aoezdemir.todoapp.crud.remote.ServiceFactory;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.OverviewViewHolder> {

    public final static int CREATE_NEW_TODO = 0;
    public final static int EDIT_TODO = 1;

    private List<Todo> todos;

    public OverviewAdapter(List<Todo> todos) {
        this.todos = todos;
    }

    @NonNull
    @Override
    public OverviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OverviewViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_todo, parent, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull OverviewViewHolder holder, int position) {
        if (todos != null && !todos.isEmpty() && position < todos.size()) {

            // Load the actual todo into the card view
            holder.loadTodo(todos.get(position), position);

            // Add a OnClickListener on the card view itself
            holder.view.setOnClickListener((View v) -> {
                Intent detailIntent = new Intent(v.getContext(), DetailviewActivity.class);
                detailIntent.putExtra(DetailviewActivity.INTENT_KEY_TODO, todos.get(position));
                v.getContext().startActivity(detailIntent);
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull OverviewViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (todos != null && !todos.isEmpty() && position < todos.size()) {
            if (payloads.isEmpty()) {
                onBindViewHolder(holder, position);
            } else {
                for (Object payload : payloads) {
                    if (payload instanceof Todo) {
                        holder.loadTodo((Todo) payload, position);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (this.todos != null) {
            return todos.size();
        }
        return 0;
    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
    }

    class OverviewViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private OverviewAdapter adapter;
        private ImageButton ibDone;
        private TextView tvTodoTitle;
        private TextView tvTodoDate;
        private ImageView ivDateIcon;
        private ImageButton ibEdit;
        private ImageButton ibFavoriteToggle;
        private ImageButton ibDelete;

        OverviewViewHolder(View v, OverviewAdapter a) {
            super(v);
            view = v;
            adapter = a;
            ibDone = view.findViewById(R.id.ibDone);
            tvTodoTitle = view.findViewById(R.id.tvTodoTitle);
            tvTodoDate = view.findViewById(R.id.tvTodoDate);
            ivDateIcon = view.findViewById(R.id.ivDateIcon);
            ibEdit = view.findViewById(R.id.ibEdit);
            ibFavoriteToggle = view.findViewById(R.id.ibFavouriteToggle);
            ibDelete = view.findViewById(R.id.ibDelete);
        }

        /**
         * Sets the title of given todo to the title of the card view.
         * If the todo is done the title color should be dark grey.
         *
         * @param todo
         */
        private void initTodoTitle(Todo todo) {
            tvTodoTitle.setText(todo.getName());
            tvTodoTitle.setTextColor(view.getResources().getColor(todo.isDone() ? R.color.colorTodoTitleDone : R.color.colorTodoTitleDefault, null));
        }

        /**
         * Sets the expiry date of the given todo to a textview of the card view.
         *
         * @param todo
         */
        private void initTodoDate(Todo todo) {
            int textColor = todo.isDone() ? R.color.colorTodoDateDefault : todo.isExpired() ? R.color.colorTodoDateExpired : R.color.colorTodoDateDefault;
            tvTodoDate.setText(todo.formatExpiry());
            tvTodoDate.setTextColor(view.getResources().getColor(textColor, null));
            ivDateIcon.setImageDrawable(view.getResources().getDrawable(todo.isDone() ? R.drawable.ic_event_note_dark_gray_24dp : todo.isExpired() ? R.drawable.ic_event_note_red_24dp : R.drawable.ic_event_note_dark_gray_24dp, null));
        }

        /**
         * Based on the done status of the todo, a different image will be display to show the current status.
         * After a click on the element a request will be sent to the API to change the status of the todo.
         * If something went wrong the changes will be reseted.
         *
         * @param todo Todo which state was changed
         * @param position Position of the element in the RecyclerView for updating the UI
         */
        private void initTodoDoneToggle(Todo todo, int position) {
            ibDone.setImageResource(todo.isDone() ? R.drawable.ic_check_circle_green_24dp : todo.isExpired() ? R.drawable.ic_error_outline_red_24dp : R.drawable.ic_radio_button_not_done_green_24dp);
            ibDone.setOnClickListener((View v) -> {
                todo.setDone(!todo.isDone());
                ServiceFactory.getServiceTodo().update(todo.getId(), todo).enqueue(new Callback<Todo>() {
                    @Override
                    public void onResponse(Call<Todo> call, Response<Todo> response) {
                        if (response.isSuccessful()) {
                            adapter.notifyItemChanged(position);
                        } else {
                            Toast.makeText(view.getContext(), "Error: Todo status was not changed.", Toast.LENGTH_SHORT).show();
                            todo.setDone(!todo.isDone());
                        }
                    }

                    @Override
                    public void onFailure(Call<Todo> call, Throwable t) {
                        Toast.makeText(view.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        todo.setDone(!todo.isDone());
                    }
                });
            });
        }

        /**
         * Based on the favourite status of the todo, a different image will be display to show the current status.
         * The favourite toggle should only be visible if the todo is not done yet.
         * If something went wrong the changes will be reseted.
         *
         * @param todo Todo which favourite state was changed
         * @param position Position of the element in the RecyclerView for updating the UI
         */
        private void initTodoFavouriteToggle(Todo todo, int position) {
            ibFavoriteToggle.setVisibility(todo.isDone() ? View.INVISIBLE : View.VISIBLE);
            ibFavoriteToggle.setImageResource(todo.isFavourite() ? R.drawable.ic_favorite_red_24dp : R.drawable.ic_favorite_border_dark_gray_24dp);
            ibFavoriteToggle.setOnClickListener((View v) -> {
                todo.setFavourite(!todo.isFavourite());
                ServiceFactory.getServiceTodo().update(todo.getId(), todo).enqueue(new Callback<Todo>() {
                    @Override
                    public void onResponse(Call<Todo> call, Response<Todo> response) {
                        if (response.isSuccessful()) {
                            adapter.notifyItemChanged(position);
                            ibFavoriteToggle.setVisibility(todo.isDone() ? View.INVISIBLE : View.VISIBLE);
                        } else {
                            Toast.makeText(view.getContext(), "Error: Failed to change favourite state.", Toast.LENGTH_SHORT).show();
                            todo.setFavourite(!todo.isFavourite());
                        }
                    }

                    @Override
                    public void onFailure(Call<Todo> call, Throwable t) {
                        Toast.makeText(view.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        todo.setFavourite(!todo.isFavourite());
                    }
                });
            });
        }

        private void initTodoEdit(Todo todo) {
            ibEdit.setVisibility(todo.isDone() ? View.INVISIBLE : View.VISIBLE);
            ibEdit.setOnClickListener((View v) -> {
                Intent editIntent = new Intent(view.getContext(), EditActivity.class);
                editIntent.putExtra(EditActivity.INTENT_KEY_TODO, todo);
                ((Activity) view.getContext()).startActivityForResult(editIntent, EDIT_TODO);
            });
        }

        /**
         * Initializes the delete button on the overview todo list.
         * Will be shown if todo is done.
         *
         * @param todo To be deleted
         * @param position For updating/notify the adapter
         */
        private void initTodoDelete(Todo todo, int position) {
            ibDelete.setVisibility(todo.isDone() ? View.VISIBLE : View.INVISIBLE);
            ibDelete.setOnClickListener((View v) -> {
                todos.remove(position);
                ServiceFactory.getServiceTodo().delete(todo.getId()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            adapter.notifyItemRemoved(position);
                            ibDelete.setVisibility(todo.isDone() ? View.VISIBLE : View.INVISIBLE);
                        } else {
                            Toast.makeText(view.getContext(), "Error: Failed to delete todo.", Toast.LENGTH_SHORT).show();
                            todos.add(position, todo);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(view.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        todos.add(position, todo);
                    }
                });
            });
        }

        /**
         * Evaluates the given todo and defines the visual outcome of the card view.
         *
         * @param todo     Todo to be displayed in the list
         * @param position Index of the todo in the list
         */
        void loadTodo(Todo todo, int position) {
            initTodoTitle(todo);
            initTodoDate(todo);
            initTodoDoneToggle(todo, position);
            initTodoFavouriteToggle(todo, position);
            initTodoEdit(todo);
            initTodoDelete(todo, position);
        }
    }
}
