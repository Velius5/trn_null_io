package com.nullio.opinieallegro.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.nullio.opinieallegro.AllegroApiInterface;
import com.nullio.opinieallegro.Constants;
import com.nullio.opinieallegro.R;
import com.nullio.opinieallegro.adapter.WatchedItemsListAdapter;
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
        setTitle("Lista obserwowanych przedmiotów");
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
                try {
                    for (OfferResponse offer : watchedListResponse.getOffers()) {
                        watchedItems.add(offer);
                    }
                } catch (NullPointerException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(WatchedActivity.this, "Sesja wygasła. Zaloguj się ponownie", Toast.LENGTH_SHORT).show();
                            setLoggedOff();
                        }
                    });
                    finish();
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
            final WatchedItemsListAdapter adapter = new WatchedItemsListAdapter(WatchedActivity.this, watchedItems);
            list = (ListView) findViewById(R.id.watchedListContainer);
            list.setAdapter(adapter);
        }
    }

    private void setLoggedOff() {
        SharedPreferences settings = getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("logged", 0);
        editor.commit();
    }
}
