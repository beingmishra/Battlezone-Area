package com.sahil.gupte.poobgtournament;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TournamentUtils {

    public Map<String, String> getTournamentsList(DataSnapshot dataSnapshot) {

        final Map<String, String> tournamentsList = new HashMap<>();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String name = Objects.requireNonNull(ds.child("name").getValue()).toString();
            String TID = Objects.requireNonNull(ds.child("TID").getValue()).toString();
            tournamentsList.put(TID, name);
        }

        return tournamentsList;
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
            }
        }
        return tournamentDetails;
    }
}
