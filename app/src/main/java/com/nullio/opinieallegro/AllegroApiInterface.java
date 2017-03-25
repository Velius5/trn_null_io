package com.nullio.opinieallegro;

import com.nullio.opinieallegro.model.Item;
import com.nullio.opinieallegro.transfer.BoughtResponse;
import com.nullio.opinieallegro.transfer.LoginResponse;
import com.nullio.opinieallegro.transfer.WatchedListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AllegroApiInterface {

    @FormUrlEncoded
    @POST("v1/allegro/login?access_token=" + Constants.userToken)
    Call<LoginResponse> login(@Field("userLogin") String login, @Field("hashPass") String password);


    @GET("/v1/allegro/my/bought")
    Call<BoughtResponse> getBought(@Header("Authorization") String auth);

    @GET("/v1/allegro/my/watched/active")
    Call<WatchedListResponse> getActiveWatched(@Header("Authorization") String auth);
}
