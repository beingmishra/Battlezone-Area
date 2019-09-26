package com.sahil.gupte.poobgtournament;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    private String TID, UID, DisplayName, Tname;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        if (getArguments() != null) {
            TID = getArguments().getString("TID");
            UID = getArguments().getString("UID");
            Tname = getArguments().getString("name");
            DisplayName = getArguments().getString("Username");
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

        TextView title = view.findViewById(R.id.title);
        title.setText(Tname);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (TID != null) {

            final DatabaseReference tournamentsNode = database.getReference("Tournaments");
            final DatabaseReference requestsNode = database.getReference("Requests").child("Participate");

            final Button participate = view.findViewById(R.id.participate);
            //Initialise participated to true by default to prevent sending request without checking if the user has participated or not
            setDisabled(participate, "Partcipate");
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
                        setDisabled(participate, "Participated");
                    } else {
                        setEnabled(participate, "Participate");
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
        final AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        return dialog;
    }

    private void setDisabled(Button button, String newText) {
        button.setEnabled(false);
        button.setBackground(mContext.getDrawable(R.drawable.button_grayed));
        button.setText(newText);
    }

    private void setEnabled(Button button, String newText) {
        button.setEnabled(true);
        button.setBackground(mContext.getDrawable(R.drawable.button));
        button.setText(newText);
    }
}
