package com.nullio.opinieallegro;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.nullio.opinieallegro.model.BoughtItem;
import com.nullio.opinieallegro.transfer.BoughtResponse;
import com.nullio.opinieallegro.transfer.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BoughtLoader {

    private List<BoughtItem> boughtItems;
    public static final String BASE_URL = "https://api.natelefon.pl/";

    public BoughtLoader(){
        init();
    }

    private void init(){
        AllegroApiInterface mApiService = this.getInterfaceService();
        Call<BoughtResponse> mService = mApiService.getBought("Bearer " + Constants.userToken);
        mService.enqueue(new Callback<BoughtResponse>() {
            @Override
            public void onResponse(Call<BoughtResponse> call, Response<BoughtResponse> response) {
                Log.i("RESPONSE", "info: " + response.message() + " " + response.code());
                BoughtResponse boughtResponse = response.body();
                try {
                    int returnedCount = boughtResponse.count;
                    Log.i("TAG", "MAM " + returnedCount);
                }
                catch(NullPointerException e){
                    Log.i("TAG", "BLAD");
                }
            }
            @Override
            public void onFailure(Call<BoughtResponse> call, Throwable t) {
                call.cancel();
                Log.i("Error", "Wiadomosc : " + call + " ---- " + t);
            }
        });
    }

    private AllegroApiInterface getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final AllegroApiInterface mInterfaceService = retrofit.create(AllegroApiInterface.class);
        return mInterfaceService;
    }


}
