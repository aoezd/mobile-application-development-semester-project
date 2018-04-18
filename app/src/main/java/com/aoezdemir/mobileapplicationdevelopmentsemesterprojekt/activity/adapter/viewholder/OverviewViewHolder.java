package com.aoezdemir.mobileapplicationdevelopmentsemesterprojekt.activity.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aoezdemir.mobileapplicationdevelopmentsemesterprojekt.R;

public class OverviewViewHolder extends RecyclerView.ViewHolder {

    private TextView textview;

    public OverviewViewHolder(View view) {
        super(view);
        textview = view.findViewById(R.id.textView);
    }

    public TextView getTextview() {
        return textview;
    }
}
