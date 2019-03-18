package com.rx1226.appupdater;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.rx1226.appupdater.AppUpdater;
import com.github.rx1226.appupdater.Update;

public class MainActivity extends AppCompatActivity {
    private String TAG = "AppUpdater";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppUpdater.getLatestAppVersion(this, new AppUpdater.OnFinishListener() {
            @Override
            public void onSuccess(Update update) {
                // use update to get info
                Log.d(TAG, "getLatestVersion " + update.getLatestVersion());
                Log.d(TAG, "getReleaseNotes " + update.getReleaseNotes());
                Log.d(TAG, "getUrlToDownload " + update.getStoreUrl());
            }

            @Override
            public void onFailed(String errorMessage) {
                // when error to do something
                Log.d(TAG, "ErrorMessage =  " + errorMessage);
            }
        });
    }
}
