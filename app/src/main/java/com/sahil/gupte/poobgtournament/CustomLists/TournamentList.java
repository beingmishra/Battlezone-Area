package com.sahil.gupte.poobgtournament.CustomLists;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.sahil.gupte.poobgtournament.Activities.OngoingTournamentActivity;
import com.sahil.gupte.poobgtournament.Constants;
import com.sahil.gupte.poobgtournament.Fragments.TournamentDialogFragment;
import com.sahil.gupte.poobgtournament.R;
import com.sahil.gupte.poobgtournament.Utils.TournamentUtils;

import java.util.ArrayList;
import java.util.Map;

public class TournamentList extends RecyclerView.Adapter<TournamentList.RecyclerViewHolder> {
    private final FragmentManager fragmentManager;
    private ArrayList<String> tournamentList;

    public void setDataSnapshot(DataSnapshot dataSnapshot) {
        this.dataSnapshot = dataSnapshot;
    }

    private DataSnapshot dataSnapshot;
    private final boolean ongoing;
    private final Context mContext;

    public void setCount(int count) {
        this.count = count;
        notifyDataSetChanged();
    }


    private int count;

    public void setTournamentsMap(Map<String, String> tournamentsMap) {
        this.tournamentsMap = tournamentsMap;
        tournamentList = TournamentUtils.getKeyArrayList(tournamentsMap);
    }

    private Map<String, String> tournamentsMap;
    private final FirebaseUser firebaseUser;

    public TournamentList(Context mContext , FragmentManager fragmentManager, FirebaseUser firebaseUser, boolean ongoing) {
        this.fragmentManager = fragmentManager;
        this.firebaseUser = firebaseUser;
        this.ongoing = ongoing;
        this.mContext = mContext;
    }

    public TournamentList(FragmentManager fragmentManager, FirebaseUser firebaseUser, boolean ongoing) {
        this(null, fragmentManager, firebaseUser, ongoing);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tournament_list, parent, false);

        return new RecyclerViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        //Log.d("test", "onBindViewHolder: "+ tournamentsMap);
        if (tournamentsMap != null && tournamentList != null && dataSnapshot != null) {
            holder.name.setText(TournamentUtils.getTournamentDetails(tournamentList.get(position), dataSnapshot).get(Constants.nameT));
            holder.cap.setText(TournamentUtils.getTournamentDetails(tournamentList.get(position), dataSnapshot).get(Constants.cap));
            holder.time.setText(TournamentUtils.getTournamentDetails(tournamentList.get(position), dataSnapshot).get("time"));

            if (!ongoing) {
                holder.frame.setOnClickListener(view -> {
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    TournamentDialogFragment tournamentDialogFragment = new TournamentDialogFragment();
                    Bundle bundle = new Bundle();
                    String TID = TournamentUtils.getTournamentDetails(tournamentList.get(position), dataSnapshot).get(Constants.TID);
                    bundle.putString(Constants.TID, TID);
                    bundle.putString("UID", firebaseUser.getUid());
                    bundle.putString("Username", firebaseUser.getDisplayName());
                    bundle.putString(Constants.nameT, TournamentUtils.getTournamentDetails(tournamentList.get(position), dataSnapshot).get(Constants.nameT));
                    tournamentDialogFragment.setArguments(bundle);
                    tournamentDialogFragment.show(ft, "dialog");
                });
            } else {
                holder.frame.setOnClickListener(view -> {
                    Intent intent = new Intent(mContext, OngoingTournamentActivity.class);
                    intent.putExtra(Constants.TID, TournamentUtils.getTournamentDetails(tournamentList.get(position), dataSnapshot).get(Constants.TID));
                    mContext.startActivity(intent);
                });
            }
        }
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

        final TextView time;
        final TextView cap;
        final TextView name;
        final LinearLayout frame;

        RecyclerViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            time = view.findViewById(R.id.time);
            frame = view.findViewById(R.id.content_frame);
            cap = view.findViewById(R.id.cap);

        }
    }
}