package com.example.aep;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>
{

    private List<EventData> eventDataList;

    public EventAdapter(List<EventData> eventDataList) {
        this.eventDataList = eventDataList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout and create a new view holder
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        // Bind data to the UI elements in your item layout
        EventData eventData = eventDataList.get(position);
        holder.teamNameTextView.setText(eventData.getTeamName());
        holder.locationTextView.setText(eventData.getEventLocation());
        holder.dateTextView.setText(eventData.getEventDate());
        holder.timeTextView.setText(eventData.getEventTime());
    }

    @Override
    public int getItemCount() {
        return eventDataList.size();
    }

    // Create a ViewHolder class for your items
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView teamNameTextView;
        TextView locationTextView;
        TextView dateTextView;
        TextView timeTextView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize your UI elements
            teamNameTextView = itemView.findViewById(R.id.teamNameTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }
    }
}
