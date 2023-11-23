package com.example.aep;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SportsNewsViewHolder extends RecyclerView.ViewHolder{
    public ImageView imageView;
    public TextView textTitle;
    public TextView textDescription;
    public TextView textSourceName;
    public TextView textLink;

    public SportsNewsViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
        textTitle = itemView.findViewById(R.id.textTitle);
        textDescription = itemView.findViewById(R.id.textDescription);
        textSourceName = itemView.findViewById(R.id.textSourceName);
        textLink = itemView.findViewById(R.id.textLink);
    }


}
