package com.sahil.gupte.poobgtournament.CustomLists;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sahil.gupte.poobgtournament.R;
import com.sahil.gupte.poobgtournament.Utils.TournamentUtils;

import java.util.ArrayList;
import java.util.Map;

public class OngoingParticipantsList extends RecyclerView.Adapter<OngoingParticipantsList.RecyclerViewHolder> {
    public void setCount(int count) {
        this.count = count;
        notifyDataSetChanged();
    }

    public void setStatsList(ArrayList<ArrayList<String>> statsList) {
        this.statsList = statsList;
    }

    private ArrayList<ArrayList<String>> statsList;

    private int count;

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

        final TextView kills;
        final TextView deaths;
        final TextView participant;

        RecyclerViewHolder(View view) {
            super(view);
            kills = view.findViewById(R.id.kills);
            deaths = view.findViewById(R.id.deaths);
            participant = view.findViewById(R.id.participant);
        }
    }
}