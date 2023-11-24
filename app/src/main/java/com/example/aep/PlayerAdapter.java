package com.example.aep;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    private List<Player> playerList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Player player);
    }

    public PlayerAdapter(List<Player> playerList, OnItemClickListener listener) {
        this.playerList = playerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Player player = playerList.get(position);
        Log.i("PLayerAdapter",player.getFull_name());
        Log.i("PLayerAdapter","blancc");
        holder.bind(player, listener);
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewPlayerName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPlayerName = itemView.findViewById(R.id.textViewPlayerName);
        }

        void bind(final Player player, final OnItemClickListener listener) {
            textViewPlayerName.setText(player.getFull_name());
            Log.i("PLayerAdapter",player.getFull_name());
            Log.i("PLayerAdapter","blancc1");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("PLayerAdapter",player.getFull_name());
                    Log.i("PLayerAdapter","blancc2");
                    listener.onItemClick(player);
                }
            });
        }
    }

}
