package com.sahil.gupte.poobgtournament;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowTournamentFragment extends Fragment {

    private Map<String, String> tournamentsList;


    public ShowTournamentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Log.d("test", "onCreateView: view created");
        View view = inflater.inflate(R.layout.fragment_show_tournament, container, false);

        final RecyclerView list = view.findViewById(R.id.tournaments_list);
        final TournamentList listAdapter = new TournamentList(getActivity(), getFragmentManager(), firebaseUser);
        list.setAdapter(listAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);
        list.setLayoutManager(llm);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference tournamentsNode = database.getReference("Tournaments");

        tournamentsNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TournamentUtils tournamentUtils = new TournamentUtils();

                tournamentsList = tournamentUtils.getTournamentsList(dataSnapshot);

                Log.d("test", "onDataChange: "+tournamentUtils.getParticipants(dataSnapshot, "Tournament1"));

                listAdapter.setDataSnapshot(dataSnapshot);
                listAdapter.setTournamentsMap(tournamentsList);
                listAdapter.setCount(tournamentsList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

}
