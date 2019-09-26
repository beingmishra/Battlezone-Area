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


/**
 * A simple {@link Fragment} subclass.
 */
public class OngoingFragment extends Fragment {


    public OngoingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        View view = inflater.inflate(R.layout.fragment_ongoing, container, false);

        final RecyclerView list = view.findViewById(R.id.ongoing_tournaments_list);
        final TournamentList listAdapter = new TournamentList(getActivity(), getFragmentManager(), firebaseUser, true);
        list.setAdapter(listAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);
        list.setLayoutManager(llm);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ongoingNode = database.getReference(Constants.OngoingNode);

        ongoingNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TournamentUtils tournamentUtils = new TournamentUtils();

                //Contains list of all tournaments under Ongoing
                Map<String, String> tournamentsList = tournamentUtils.getTournamentsList(dataSnapshot);

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
