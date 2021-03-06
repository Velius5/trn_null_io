package com.nullio.opinieallegro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nullio.opinieallegro.transfer.LoginResponse;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends Activity {

    public static final String BASE_URL = "https://api.natelefon.pl/";

    private Button loginButton;
    private EditText loginEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        loginEditText = (EditText) findViewById(R.id.input_login);
        passwordEditText = (EditText) findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    loginProcessWithRetrofit(loginEditText.getText().toString(), CryptoUtils.SHA256(passwordEditText.getText().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    private void loginProcessWithRetrofit(final String login, String password) throws Exception{
        AllegroApiInterface mApiService = this.getInterfaceService();
        Call<LoginResponse> mService = mApiService.login(login, password);
        mService.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.i("RESPONSE", "info: " + response.message() + " " + response.code());
                LoginResponse mLoginObject = response.body();
                try {
                    String returnedResponse = mLoginObject.userId;
                    Toast.makeText(LoginActivity.this, "Zalogowano uzytkownika o id " + returnedResponse, Toast.LENGTH_LONG).show();
                    setLoggedIn(returnedResponse);
                    Intent intent = new Intent(LoginActivity.this, ItemsList.class);
                    startActivity(intent);
                    finish();
                }
                catch(NullPointerException e){
                    Toast.makeText(LoginActivity.this, "Błędny login lub hasło", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                call.cancel();
                Log.i("Error", "Wiadomosc : " + call + " ---- " + t);
                Toast.makeText(LoginActivity.this, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setLoggedIn(String userId){
        SharedPreferences settings = getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("logged", new Date().getTime());
        editor.putString("userId", userId);
        editor.commit();
        finish();
    }
}
