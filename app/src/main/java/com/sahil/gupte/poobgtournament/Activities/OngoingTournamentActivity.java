package com.sahil.gupte.poobgtournament.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sahil.gupte.poobgtournament.Constants;
import com.sahil.gupte.poobgtournament.CustomLists.OngoingParticipantsList;
import com.sahil.gupte.poobgtournament.R;
import com.sahil.gupte.poobgtournament.Utils.TournamentUtils;

public class OngoingTournamentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_tournament);

        Intent intent = getIntent();
        String TID = intent.getStringExtra(Constants.TID);

        if (TID == null) {
            finish();
        }

        final RecyclerView list = findViewById(R.id.ongoing_tournaments_details);
        final OngoingParticipantsList listAdapter = new OngoingParticipantsList();
        list.setAdapter(listAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        list.setLayoutManager(llm);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        assert TID != null;
        final DatabaseReference tournamentsNode = database.getReference(Constants.OngoingNode).child(TID);

        tournamentsNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TournamentUtils tournamentUtils = new TournamentUtils();
                listAdapter.setStatsList(tournamentUtils.getOngoingDetails(dataSnapshot));
                listAdapter.setCount(tournamentUtils.getOngoingDetails(dataSnapshot).size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
