package com.nullio.opinieallegro.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nullio.opinieallegro.R;
import com.nullio.opinieallegro.adapter.ReviewsApadter;
import com.nullio.opinieallegro.model.ReviewDisplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemReviewsActivity extends AppCompatActivity {
    public static final String OFFER_ID = "offerId";
    private ListView listView;
    List<ReviewDisplay> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_reviews);
        listView = (ListView) findViewById(R.id.listView);
        reviews = new ArrayList<>();
        loadReviews();
    }

    private void loadReviews() {
        String offerId = getIntent().getStringExtra(OFFER_ID);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("offers").child(offerId).child("reviews").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Boolean> resultMap = (Map<String, Boolean>) dataSnapshot.getValue();
                if (resultMap != null) {
                    for (Map.Entry<String, Boolean> entry : resultMap.entrySet()) {
                        getReviewDetails(entry.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getReviewDetails(String key) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("reviews").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ReviewDisplay r = dataSnapshot.getValue(ReviewDisplay.class);
                reviews.add(r);
                refreshUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void refreshUI() {
        ReviewsApadter adapter = new ReviewsApadter(this, reviews);
        listView.setAdapter(adapter);
    }
}
