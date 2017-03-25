package com.nullio.opinieallegro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;

import com.nullio.opinieallegro.model.BoughtItem;
import com.nullio.opinieallegro.transfer.BoughtResponse;
import com.nullio.opinieallegro.transfer.OfferResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BoughtLoader {
    private List<BoughtItem> boughtItems;
    private Context context;
    private String result;
    public static final String BASE_URL = "https://api.natelefon.pl/";

    public BoughtLoader(Context context) {
        this.context = context;
        boughtItems = new ArrayList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<BoughtItem> getBoughtItems() {
        return boughtItems;
    }

    private void init() throws Exception {
        AllegroApiInterface mApiService = this.getInterfaceService();
        Call<BoughtResponse> mService = mApiService.getBought("Bearer " + Constants.userToken);
        Response<BoughtResponse> boughtResponse = null;
        boughtResponse = mService.execute();
        try {
            List<OfferResponse> offers = boughtResponse.body().getOffers();
            for (OfferResponse offer : offers) {
                String picture = "";
                if (offer.getMain() != null) {
                    picture = offer.getMain().getMedium();
                }
                boughtItems.add(new BoughtItem(offer.getId(), offer.getName(), picture));
                Log.i("TAG", "id " + offer.getId());
            }
            result = "success";
            //Log.i("TAG", boughtResponse.body().getOffers().get(0).getMain().getMedium());
        } catch (Exception e) {
            if (boughtResponse.code() == 403) {
                Log.i("TAG", "Wymagane ponowne zalogowanie");
                setLoggedOff();
                result = "login";
            }
            e.printStackTrace();
            Log.i("TAG", "BLAD " + boughtResponse.message() + " " + boughtResponse.code());
        }
    }

    private AllegroApiInterface getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final AllegroApiInterface mInterfaceService = retrofit.create(AllegroApiInterface.class);
        return mInterfaceService;
    }

    private void setLoggedOff() {
        SharedPreferences settings = context.getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("logged", 0);
        editor.commit();
    }

    public String getResult() {
        return result;
    }
}
