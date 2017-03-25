package com.nullio.opinieallegro.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nullio.opinieallegro.R;
import com.nullio.opinieallegro.adapter.ReviewsApadter;
import com.nullio.opinieallegro.model.ReviewDisplay;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReviewDetailsActivity extends AppCompatActivity {
    public static final String OFFER_TITLE = "offer_title";
    public static final String OFFER_PHOTO = "offer_photo";
    public static final String OFFER_CONTENT = "offer_content";
    private TextView content;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_details);
        content = (TextView) findViewById(R.id.content);
        image = (ImageView) findViewById(R.id.image);
        String offerTitle = getIntent().getStringExtra(OFFER_TITLE);
        String offerPhoto = getIntent().getStringExtra(OFFER_PHOTO);
        String offerContent = getIntent().getStringExtra(OFFER_CONTENT);
        setTitle(offerTitle);
        content.setText(offerContent);
        if (!offerPhoto.equals("")) {
            Picasso.with(this).load(offerPhoto).into(image);
        }
    }

}
