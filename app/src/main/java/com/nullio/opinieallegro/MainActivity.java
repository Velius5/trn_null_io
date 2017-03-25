package com.nullio.opinieallegro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nullio.opinieallegro.activity.WatchedActivity;
import com.nullio.opinieallegro.point.service.PointsService;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button addReviewButton, loginButton, watchedItemsButton;
    private TextView pointsTextView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Opinie Allegro");
        addReviewButton = (Button) findViewById(R.id.addReviewButton);
        pointsTextView = (TextView) findViewById(R.id.points);
        watchedItemsButton = (Button) findViewById(R.id.watchedItemsButton);
        addReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("settings", 0);
                long lastLoggedIn = settings.getLong("logged", 0l);
                if (new Date().getTime() - lastLoggedIn < 1000 * 60 * 60 * 24) {
                    Intent intent = new Intent(MainActivity.this, ItemsList.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        watchedItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("settings", 0);
                long lastLoggedIn = settings.getLong("logged", 0l);
                if (new Date().getTime() - lastLoggedIn < 1000 * 60 * 60 * 24) {
                    Intent intent = new Intent(MainActivity.this, WatchedActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        firebaseAuth();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences settings = getSharedPreferences("settings", 0);
        String userId = settings.getString("userId", "none");
        PointsService pointsService = new PointsService();
        pointsService.getPoints(userId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    setPoints((Long) dataSnapshot.getValue());
                } else {
                    setPoints(0l);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void firebaseAuth() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("Main activity", "signInAnonymously:FAILURE", exception);
                    }
                });
    }

    private void setPoints(Long points) {
        pointsTextView.setText(points + " pkt");
    }
}
