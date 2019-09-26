package com.sahil.gupte.poobgtournament;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class TournamentUtils {

    public static Map<String, String> UserNames = new HashMap<>();

    public Map<String, String> getTournamentsList(DataSnapshot dataSnapshot) {

        final Map<String, String> tournamentsList = new HashMap<>();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String name = Objects.requireNonNull(ds.child("name").getValue()).toString();
            String TID = Objects.requireNonNull(ds.child("TID").getValue()).toString();
            tournamentsList.put(TID, name);
        }

        return tournamentsList;
    }

    public ArrayList<ArrayList<String>> getOngoingDetails(DataSnapshot dataSnapshot) {
        final ArrayList<ArrayList<String>> ongoingDetails = new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.child("Participants").getChildren()) {
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

    public static void populateNameFromUID () {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference usersNode = database.getReference("Users");
        usersNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d("test", "onDataChange: after countdown "+ds);
                    UserNames.put(ds.getKey(), Objects.requireNonNull(ds.getValue()).toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public Map<String, String> getParticipants (DataSnapshot dataSnapshot, String TID) {

        Map<String, String> participants = new HashMap<>();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (Objects.equals(ds.getKey(), TID)) {
                for (DataSnapshot dsPart : ds.getChildren()) {
                    if (Objects.equals(dsPart.getKey(), "Participants")) {
                        for (DataSnapshot dsParticipants : dsPart.getChildren()) {
                            participants.put(dsParticipants.getKey(), Objects.requireNonNull(dsParticipants.getValue()).toString());
                        }
                    }
                }
            }
        }

        return participants;
    }

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

    public static ArrayList<String> getKeyArrayList(Map<String, String> map) {

        return new ArrayList<>(map.keySet());
    }

    public static ArrayList<String> getValuesArrayList(Map<String, String> map) {

        return new ArrayList<>(map.values());
    }

    public static Map<String, String> getTournamentDetails(String TID, DataSnapshot dataSnapshot) {
        Map<String, String> tournamentDetails = new HashMap<>();
        for (DataSnapshot ds: dataSnapshot.child(TID).getChildren()) {
            switch (Objects.requireNonNull(ds.getKey())) {
                case "name":
                    tournamentDetails.put("name", Objects.requireNonNull(ds.getValue()).toString());
                    break;

                case "time":
                    tournamentDetails.put("time", Objects.requireNonNull(ds.getValue()).toString());
                    break;

                case "cap":
                    tournamentDetails.put("cap", Objects.requireNonNull(ds.getValue()).toString());
                    break;

                case "TID":
                    tournamentDetails.put("TID", Objects.requireNonNull(ds.getValue()).toString());
            }
        }
        return tournamentDetails;
    }
}
