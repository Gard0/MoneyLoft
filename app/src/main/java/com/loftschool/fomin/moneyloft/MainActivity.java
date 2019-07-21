package com.loftschool.fomin.moneyloft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    public static final String AUTH_TOKEN = "auth_token";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!TextUtils.isEmpty(getToken())) {
            startBudgetActivity();
        }

        Button enterButton = findViewById(R.id.enter_button);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBudgetActivity();
            }
        });

        LoftMoneyApp loftMoneyApp = (LoftMoneyApp) getApplication();

        Api api = loftMoneyApp.getApi();

        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Call<AuthResponse> authCall = api.auth(androidId);
        authCall.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(
                    final Call<AuthResponse> call, final Response<AuthResponse> response
            ) {
                saveToken(response.body().getAuthToken());

            }

            @Override
            public void onFailure(final Call<AuthResponse> call, final Throwable t) {

            }
        });
    }

    private void startBudgetActivity() {
        startActivity(new Intent(MainActivity.this, BudgetActivity.class));
        finish();
        overridePendingTransition(R.anim.from_right_in, R.anim.alfa_out);
    }

    private void saveToken(final String token) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTH_TOKEN, token);
        editor.apply();
    }

    private String getToken() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        return sharedPreferences.getString(AUTH_TOKEN, "");
    }
}
