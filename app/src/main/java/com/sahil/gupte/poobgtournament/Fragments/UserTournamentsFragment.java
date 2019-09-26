package com.sahil.gupte.poobgtournament.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sahil.gupte.poobgtournament.Constants;
import com.sahil.gupte.poobgtournament.CustomLists.TournamentList;
import com.sahil.gupte.poobgtournament.R;
import com.sahil.gupte.poobgtournament.Utils.TournamentUtils;

import java.util.Map;

public class UserTournamentsFragment extends Fragment {


    public UserTournamentsFragment() {
        // Required empty public constructor
    }

    private String UID;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_tournaments, container, false);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        UID = user.getUid();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference tournamentsNode = database.getReference(Constants.TournamentsNode);

        final RecyclerView list = view.findViewById(R.id.user_tournaments_list);
        final TournamentList listAdapter = new TournamentList(getFragmentManager(), user, false);
        list.setAdapter(listAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);
        list.setLayoutManager(llm);

        tournamentsNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TournamentUtils tournamentUtils = new TournamentUtils();

                Map<String, String> userTournaments = tournamentUtils.getUserTournaments(dataSnapshot, UID);

                listAdapter.setDataSnapshot(dataSnapshot);
                listAdapter.setTournamentsMap(userTournaments);
                listAdapter.setCount(userTournaments.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

}
