package com.sahil.gupte.poobgtournament;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParticipantsList extends RecyclerView.Adapter<ParticipantsList.RecyclerViewHolder> {
    private final HashMap<Integer, RecyclerView.ViewHolder> holderHashMap = new HashMap<>();

    public void setCount(int count) {
        this.count = count;
        notifyDataSetChanged();
    }

    public void setParticipantsList(ArrayList<String> participantsList) {
        this.participantsList = participantsList;
    }

    private ArrayList<String> participantsList;

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

        TextView participants;

        RecyclerViewHolder(View view) {
            super(view);
            participants = view.findViewById(R.id.participants);
        }
    }
}