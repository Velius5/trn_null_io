package com.nullio.opinieallegro.point.service;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by bartoszlach on 25.03.2017.
 */
public class PointsService {
    public void getPoints(String userId, ValueEventListener listener) {
        FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("points").addListenerForSingleValueEvent(listener);
    }

    public void addPoints(final String userId, final int value) {
        FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("points").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int curVal = (int) dataSnapshot.getValue();
                curVal += value;
                FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("points").setValue(curVal);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
