package com.example.aep;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SportsNewsAdapter extends RecyclerView.Adapter<SportsNewsViewHolder>
{
    private List<NewsItem> newsItemList; // Assuming you have a NewsItem class to represent each news item

    public SportsNewsAdapter(List<NewsItem> newsItemList) {
        this.newsItemList = newsItemList;
    }

    @NonNull
    @Override
    public SportsNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_sports_news, parent, false);
        return new SportsNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SportsNewsViewHolder holder, int position) {
        NewsItem newsItem = newsItemList.get(position);

        // Set data to views in the ViewHolder
       // holder.imageView.setImageResource(newsItem.getImageUrl());
        holder.textTitle.setText(newsItem.getTitle());
        holder.textDescription.setText(newsItem.getDescription());
        holder.textSourceName.setText(newsItem.getSourceName());
        holder.textLink.setText(newsItem.getSourceUrl());
    }

    @Override
    public int getItemCount() {
        return newsItemList.size();
    }

    public void updateData(List<NewsItem> newsItemList) {
        newsItemList.clear();
        newsItemList.addAll(newsItemList);
        notifyDataSetChanged();
    }
}
