package com.sahil.gupte.poobgtournament.CustomLists;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sahil.gupte.poobgtournament.R;

import java.util.ArrayList;

public class ParticipantsList extends RecyclerView.Adapter<ParticipantsList.RecyclerViewHolder> {
    public void setCount(int count) {
        this.count = count;
        notifyDataSetChanged();
    }

    public void setParticipantsList(ArrayList<String> participantsList) {
        this.participantsList = participantsList;
    }

    private ArrayList<String> participantsList;

    private int count;

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.participants_list, parent, false);
        return new RecyclerViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        holder.participants.setText(participantsList.get(position));
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

        final TextView participants;

        RecyclerViewHolder(View view) {
            super(view);
            participants = view.findViewById(R.id.participants);
        }
    }
}