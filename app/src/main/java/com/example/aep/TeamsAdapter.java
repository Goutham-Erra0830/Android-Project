package com.example.aep;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.ViewHolder> {

    private List<Player> playerList;
    //private List<String> playerList;

    public TeamsAdapter(List<Player> playerList) {
        this.playerList = playerList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_item, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /*Player player = playerList.get(position);
        holder.textViewPlayerName.setText(player.getFull_name());
        Log.i("TeamsAdapter","ela");
        Log.i("TeamsAdapter",player.getFull_name());*/

        Object item = playerList.get(position);
        Log.i("TeamsAdapter", "Item at position " + position + " is of type " + item.getClass().getName());

        if (item instanceof Player) {
            Player player = (Player) item;
            holder.textViewPlayerName.setText(player.getFull_name());
            Log.i("TeamsAdapter","ela");
            Log.i("TeamsAdapter",player.getFull_name());
        } else {
            Log.e("TeamsAdapter", "Unexpected item type at position " + position);
        }
    }


    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewPlayerName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPlayerName = itemView.findViewById(R.id.textViewPlayerName);
        }
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
        notifyDataSetChanged();
    }


}

