package com.nullio.opinieallegro;

import com.nullio.opinieallegro.transfer.BoughtResponse;
import com.nullio.opinieallegro.transfer.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AllegroApiInterface {

    @FormUrlEncoded
    @POST("v1/allegro/login?access_token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhbGxlZ3JvX2FwaSJdLCJleHAiOjE0OTA0NTI0ODQsImp0aSI6IjcxOWE4MDM4LTljNWItNDNhMS04ZGViLWM5ZmI5ZWE3MjFkYyIsImNsaWVudF9pZCI6ImE0MWY1YjJhLThlODctNGI4Yi1iNmZlLTc0Y2M3NjM3MjBkNyJ9.REtsYH2Qtu77Ay6fPbcTlmOzbY-K4pw4N3odJijifZm2Rr48_DZi1Ex_uKLFOoND3w6ohfcKKAepqz-Y8S8oSfJBWaOjtVBRZ8zDyO0a70mM-eZ_GvzqmlJMyAhKlQK-CXhqaVdX6OqO8-3Oara1ffulgUoiW0uGsrOn7Bv_l08m3pBiCGtzEzws8jozj9wlTQkpFQf9henub13cye3M5nM49VeocqwK61oDwTR8ivP9ZC1g8Wj-2N-DUlXbm8GYQw7ac-pLHH1F6OYMTpb7JVhoEfJ8Lg-oAU82KJBm_zJWCkDopxoIV47VML7de4TNu06_R5BBWKpvB-PiKgc60g")
    Call<LoginResponse> login(@Field("userLogin") String login, @Field("hashPass") String password);


    @GET("/v1/allegro/my/bought")
    Call<BoughtResponse> getBought(@Header("Authorization") String auth);
}
