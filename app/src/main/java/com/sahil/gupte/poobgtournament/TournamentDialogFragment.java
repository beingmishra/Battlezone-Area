package com.sahil.gupte.poobgtournament;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class TournamentDialogFragment extends DialogFragment
{
    private Context mContext;
    private String TID, UID, DisplayName;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        if (getArguments() != null) {
            TID = getArguments().getString("TID");
            UID = getArguments().getString("UID");
            DisplayName = getArguments().getString("name");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.tournament_dialog, null);

        final RecyclerView list = view.findViewById(R.id.participants_list);
        final ParticipantsList listAdapter = new ParticipantsList();
        list.setAdapter(listAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);
        list.setLayoutManager(llm);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Log.d("test", "onCreateDialog: "+TID);
        if (TID != null) {

            final DatabaseReference tournamentsNode = database.getReference("Tournaments");
            final DatabaseReference requestsNode = database.getReference("Requests").child("Participate");

            final Button participate = view.findViewById(R.id.participate);
            //Initialise participated to true by default to prevent sending request without checking if the user has participated or not
            participate.setEnabled(false);
            participate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String,Object> taskMap = new HashMap<>();
                    taskMap.put(UID, DisplayName);
                    requestsNode.child(TID).setValue(taskMap);
                }
            });


            tournamentsNode.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    TournamentUtils tournamentUtils = new TournamentUtils();
                    Map<String, String> participants = tournamentUtils.getParticipants(dataSnapshot, TID);
                    int size = TournamentUtils.getValuesArrayList(participants).size();
                    ArrayList<String> arrayList = TournamentUtils.getValuesArrayList(participants);
                    if (participants.containsKey(UID)) {
                        participate.setEnabled(false);
                    } else {
                        participate.setEnabled(true);
                        participate.setText("Participated");
                    }
                    listAdapter.setParticipantsList(arrayList);
                    listAdapter.setCount(size);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        builder.setTitle("title");
        final AlertDialog dialog = builder.create();
        dialog.show();

        return dialog;
    }
}
