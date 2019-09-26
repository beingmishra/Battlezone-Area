package com.sahil.gupte.poobgtournament;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OngoingParticipantsList extends RecyclerView.Adapter<OngoingParticipantsList.RecyclerViewHolder> {
    private final HashMap<Integer, RecyclerView.ViewHolder> holderHashMap = new HashMap<>();

    public void setCount(int count) {
        this.count = count;
        notifyDataSetChanged();
    }

    public void setStatsList(ArrayList<ArrayList<String>> statsList) {
        this.statsList = statsList;
    }

    private ArrayList<ArrayList<String>> statsList;

    private int count;

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerViewHolder holder) {
        holderHashMap.put(holder.getAdapterPosition(), holder);
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerViewHolder holder) {
        holderHashMap.remove(holder.getAdapterPosition());
        super.onViewAttachedToWindow(holder);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ongoing_participants_list, parent, false);
        return new RecyclerViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        Map<String, String> UserNames = TournamentUtils.UserNames;
        Log.d("test", "onBindViewHolder: "+UserNames.get(statsList.get(position).get(2)));
        holder.participant.setText(UserNames.get(statsList.get(position).get(2)));
        holder.kills.setText(statsList.get(position).get(0));
        holder.deaths.setText(statsList.get(position).get(1));
    }

    @Override
    public int getItemCount() {
        return count;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView kills;
        TextView deaths;
        TextView participant;

        RecyclerViewHolder(View view) {
            super(view);
            kills = view.findViewById(R.id.kills);
            deaths = view.findViewById(R.id.deaths);
            participant = view.findViewById(R.id.participant);
        }
    }
}