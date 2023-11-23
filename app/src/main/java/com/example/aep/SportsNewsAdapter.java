package com.example.aep;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SportsNewsAdapter extends RecyclerView.Adapter<SportsNewsAdapter.SportsNewsViewHolder>
{
    private List<NewsItem> newsList;

    public SportsNewsAdapter(List<NewsItem> newsList) {

        this.newsList = newsList;
    }

    public void setItems(List<NewsItem> newsList) {
        this.newsList.clear();  // Clear existing items
        this.newsList.addAll(newsList);  // Add new items
        notifyDataSetChanged();
    }

    public SportsNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout here and return a new ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_sports_news, parent, false);
        return new SportsNewsViewHolder(view);
    }

    public void onBindViewHolder(@NonNull SportsNewsViewHolder holder, int position) {
        NewsItem newsItem = newsList.get(position);

        // Set data to views in the ViewHolder
        // holder.imageView.setImageResource(newsItem.getImageUrl());
        holder.titleTextView.setText(newsItem.getTitle());
        Log.i("SportsnewsAdapter", newsItem.getTitle());
        holder.descriptionTextView.setText(newsItem.getDescription());
        Log.d("sourcename", String.valueOf(newsItem.getSourceName()));
        holder.sourceName.setText(newsItem.getSourceName());
        Log.d("sourceurl", String.valueOf(newsItem.getUrl()));
        holder.sourceUrl.setText(newsItem.getUrl());
        Log.d("imageurl", String.valueOf(newsItem.getUrlToImage()));
        Glide.with(holder.itemView)
                .load(newsItem.getUrlToImage())
                .into(holder.imageUrl);

        // Set a click listener on the entire item view
        holder.itemView.setOnClickListener(v -> {
            // Get the URL of the news article
            String articleUrl = newsItem.getUrl();

            // Open the URL in the default browser
            if (articleUrl != null && !articleUrl.isEmpty()) {
                openInBrowser(articleUrl, holder.itemView.getContext());
            }
        });

    }

    private void openInBrowser(String url, Context context) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }


    @Override
    public int getItemCount() {
        Log.d("responsesize", String.valueOf(newsList.size()));
        return newsList.size();
    }

    public static class SportsNewsViewHolder extends RecyclerView.ViewHolder  {
        // Define your ViewHolder's views here
        public TextView titleTextView;
        public TextView descriptionTextView;
        public TextView sourceName;
        public TextView sourceUrl;
        public ImageView imageUrl;


        public SportsNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textTitle);
            descriptionTextView = itemView.findViewById(R.id.textDescription);
            sourceName = itemView.findViewById(R.id.textSourceName);
            sourceUrl = itemView.findViewById(R.id.textLink);
            imageUrl = itemView.findViewById(R.id.imageView);
            // Find other views by ID as needed
        }
    }
}
