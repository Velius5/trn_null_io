package com.nullio.opinieallegro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nullio.opinieallegro.adapter.BoughtItemsListAdapter;
import com.nullio.opinieallegro.model.BoughtItem;
import com.nullio.opinieallegro.transfer.BoughtResponse;
import com.nullio.opinieallegro.transfer.OfferResponse;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BoughtListActivity extends AppCompatActivity {
    public static final String BASE_URL = "https://api.natelefon.pl/";
    private ListView itemsListView;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);
        itemsListView = (ListView) findViewById(R.id.list);
        setTitle("Lista zakupionych przedmiotów");
        SharedPreferences prefs = getSharedPreferences("settings", 0);
        userId = prefs.getString("userId", "0");
        GetBoughtItemsTask task = new GetBoughtItemsTask();
        task.execute();
    }

    class GetBoughtItemsTask extends AsyncTask<Void, Integer, List<OfferResponse>> {
        Retrofit retrofit;
        AllegroApiInterface allegroApiInterface;
        Call<BoughtResponse> call;
        BoughtResponse boughtListResponse = null;
        List<OfferResponse> boughtItems = new ArrayList<>();

        @Override
        protected List<OfferResponse> doInBackground(Void... params) {
            try {
                boughtListResponse = call.execute().body();
                try {
                    for (OfferResponse offer : boughtListResponse.getOffers()) {
                        boughtItems.add(offer);
                    }
                } catch (NullPointerException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(BoughtListActivity.this, "Sesja wygasła. Zaloguj się ponownie", Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return boughtItems;
        }

        @Override
        protected void onPreExecute() {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            allegroApiInterface = retrofit.create(AllegroApiInterface.class);
            call = allegroApiInterface.getBought("Bearer " + Constants.userToken);
        }

        @Override
        protected void onPostExecute(List<OfferResponse> items) {
            final BoughtItemsListAdapter adapter = new BoughtItemsListAdapter(BoughtListActivity.this, boughtItems);
            itemsListView = (ListView) findViewById(R.id.list);
            itemsListView.setAdapter(adapter);
        }


    }

    private void filterData(final List<BoughtItem> boughtItems) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("reviews");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> userReviewsMap = (Map<String, String>) dataSnapshot.getValue();
                List<BoughtItem> newItemsList = new ArrayList<>();
                if (userReviewsMap != null) {
                    for (BoughtItem item : boughtItems) {
                        if (!userReviewsMap.containsKey(item.getId())) {
                            newItemsList.add(item);
                        }
                    }
                }
                getData(newItemsList);
            }

            private void getData(List<BoughtItem> items) {
                SingleItemAdapter adapter = new SingleItemAdapter(this, items);
                itemsListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
