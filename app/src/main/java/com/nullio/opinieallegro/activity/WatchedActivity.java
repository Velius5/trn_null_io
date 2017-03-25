package com.nullio.opinieallegro.activity;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.nullio.opinieallegro.AllegroApiInterface;
import com.nullio.opinieallegro.Constants;
import com.nullio.opinieallegro.R;
import com.nullio.opinieallegro.adapter.WatchedItemsListAdapter;
import com.nullio.opinieallegro.model.Item;
import com.nullio.opinieallegro.transfer.OfferResponse;
import com.nullio.opinieallegro.transfer.WatchedListResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WatchedActivity extends AppCompatActivity {

    public static final String BASE_URL = "https://api.natelefon.pl/";
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watched);
        GetWatchedItemsTask task = new GetWatchedItemsTask();
        task.execute();
    }

    class GetWatchedItemsTask extends AsyncTask<Void, Integer, List<OfferResponse>> {
        Retrofit retrofit;
        AllegroApiInterface allegroApiInterface;
        Call<WatchedListResponse> call;
        WatchedListResponse watchedListResponse = null;
        List<OfferResponse> watchedItems = new ArrayList<>();

        @Override
        protected List<OfferResponse> doInBackground(Void... params) {
            try {
                watchedListResponse = call.execute().body();
                System.out.println(watchedListResponse);
                for(OfferResponse offer : watchedListResponse.getOffers()) {
                    watchedItems.add(offer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return watchedItems;
        }

        @Override
        protected void onPreExecute() {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            allegroApiInterface = retrofit.create(AllegroApiInterface.class);
            call = allegroApiInterface.getActiveWatched("Bearer " + Constants.userToken);
        }

        @Override
        protected void onPostExecute(List<OfferResponse> items) {
            System.out.println("POST EXECUTE");
            for (OfferResponse item : watchedItems) {
                System.out.println(item.getName());
            }
            final WatchedItemsListAdapter adapter = new WatchedItemsListAdapter(WatchedActivity.this, watchedItems);
            list = (ListView) findViewById(R.id.watchedListContainer);
            list.setAdapter(adapter);
        }
    }
}
