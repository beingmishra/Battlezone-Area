package com.sahil.gupte.poobgtournament.Utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sahil.gupte.poobgtournament.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TournamentUtils {

    public static final Map<String, String> UserNames = new HashMap<>();

    public static void populateNameFromUID() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference usersNode = database.getReference(Constants.UsersNode);
        usersNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserNames.put(ds.getKey(), Objects.requireNonNull(ds.getValue()).toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //DataSnapshot is of TournamentsNode
    public static Map<String, String> getTournamentDetails(String TID, DataSnapshot dataSnapshot) {
        Map<String, String> tournamentDetails = new HashMap<>();
        for (DataSnapshot ds : dataSnapshot.child(TID).getChildren()) {
            switch (Objects.requireNonNull(ds.getKey())) {
                case "name":
                    tournamentDetails.put(Constants.nameT, Objects.requireNonNull(ds.getValue()).toString());
                    break;

                case "time":
                    tournamentDetails.put("time", Objects.requireNonNull(ds.getValue()).toString());
                    break;

                case "cap":
                    tournamentDetails.put(Constants.cap, Objects.requireNonNull(ds.getValue()).toString());
                    break;

                case "TID":
                    tournamentDetails.put(Constants.TID, Objects.requireNonNull(ds.getValue()).toString());
            }
        }
        return tournamentDetails;
    }

    //DataSnapshot is of TournamentsNode
    public Map<String, String> getTournamentsList(DataSnapshot dataSnapshot) {

        final Map<String, String> tournamentsList = new HashMap<>();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String name = Objects.requireNonNull(ds.child(Constants.nameT).getValue()).toString();
            String TID = Objects.requireNonNull(ds.child(Constants.TID).getValue()).toString();
            tournamentsList.put(TID, name);
        }

        return tournamentsList;
    }

    //DataSnapshot is of tournament inside OngoingNode
    public ArrayList<ArrayList<String>> getOngoingDetails(DataSnapshot dataSnapshot) {
        final ArrayList<ArrayList<String>> ongoingDetails = new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.child(Constants.ParticipantsNode).getChildren()) {
            String UID = ds.getKey();
            Log.d("test", "getOngoingDetails: "+ds.child("kills"));
            String kills = Objects.requireNonNull(ds.child("kills").getValue()).toString();
            String deaths = Objects.requireNonNull(ds.child("deaths").getValue()).toString();
            ongoingDetails.add(new ArrayList<String>() {{
                add(kills);
                add(deaths);
                add(UID);
            }});
        }
        return ongoingDetails;
    }

    //DataSnapshot is of TournamentsNode
    public Map<String, String> getParticipants (DataSnapshot dataSnapshot, String TID) {

        Map<String, String> participants = new HashMap<>();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (Objects.equals(ds.getKey(), TID)) {
                for (DataSnapshot dsPart : ds.getChildren()) {
                    if (Objects.equals(dsPart.getKey(), Constants.ParticipantsNode)) {
                        for (DataSnapshot dsParticipants : dsPart.getChildren()) {
                            participants.put(dsParticipants.getKey(), Objects.requireNonNull(dsParticipants.getValue()).toString());
                        }
                    }
                }
            }
        }

        return participants;
    }

    public static ArrayList<String> getKeyArrayList(Map<String, String> map) {

        return new ArrayList<>(map.keySet());
    }

    public static ArrayList<String> getValuesArrayList(Map<String, String> map) {

        return new ArrayList<>(map.values());
    }

    //DataSnapshot is of TournamentsNode
    public Map<String, String> getUserTournaments(DataSnapshot dataSnapshot, String UID) {
        Map<String, String> userTournaments = new HashMap<>();
        Map<String, String> tournamentList = getTournamentsList(dataSnapshot);
        for (String TID : tournamentList.keySet()) {
            Map<String, String> participants = getParticipants(dataSnapshot, TID);
            if (participants.containsKey(UID)) {
                userTournaments.put(TID, tournamentList.get(TID));
            }
        }
        return userTournaments;
    }
}
