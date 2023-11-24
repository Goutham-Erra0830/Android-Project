package com.example.aep;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
public class EditPlayerAdapter extends RecyclerView.Adapter<EditPlayerAdapter.ViewHolder> {
    private static final int MAX_SELECTED_PLAYERS = 13;
    private List<Player> playerList;
    private List<Player> selectedPlayers;

    public EditPlayerAdapter(List<Player> playerList) {
        this.playerList = playerList;
        this.selectedPlayers = new ArrayList<>();
    }
    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }
    public List<Player> getSelectedPlayers() {
        return selectedPlayers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edititem_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Player player = playerList.get(position);
        holder.bind(player);
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }

        public void bind(Player player) {
            checkBox.setText(player.getFull_name());
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Update the selectedPlayers list based on user selection
                if (isChecked) {
                    if (selectedPlayers.size() < MAX_SELECTED_PLAYERS) {
                        selectedPlayers.add(player);
                    } else {
                        checkBox.setChecked(false); // Uncheck if the limit is reached
                    }
                } else {
                    selectedPlayers.remove(player);
                }
            });
        }
    }

}
