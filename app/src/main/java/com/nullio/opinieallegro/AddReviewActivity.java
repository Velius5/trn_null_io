package com.nullio.opinieallegro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AddReviewActivity extends AppCompatActivity {
    public static final String OFFER_ID = "offerId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
    }
}
