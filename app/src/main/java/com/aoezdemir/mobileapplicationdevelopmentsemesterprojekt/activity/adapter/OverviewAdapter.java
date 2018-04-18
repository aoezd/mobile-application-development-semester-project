package com.aoezdemir.mobileapplicationdevelopmentsemesterprojekt.activity.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aoezdemir.mobileapplicationdevelopmentsemesterprojekt.R;
import com.aoezdemir.mobileapplicationdevelopmentsemesterprojekt.activity.adapter.viewholder.OverviewViewHolder;
import com.aoezdemir.mobileapplicationdevelopmentsemesterprojekt.model.Todo;

import java.util.List;

public class OverviewAdapter extends RecyclerView.Adapter<OverviewViewHolder> {

    private static final String TAG = OverviewAdapter.class.getSimpleName();

    private List<Todo> todos;

    public OverviewAdapter(List<Todo> todos) {
        this.todos = todos;
    }

    @NonNull
    @Override
    public OverviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OverviewViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_todo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OverviewViewHolder holder, int position) {
        holder.getTextview().setText(todos.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (this.todos != null) {
            return todos.size();
        }
        return 0;
    }
}
