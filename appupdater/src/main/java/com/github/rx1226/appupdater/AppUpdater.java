package com.github.rx1226.appupdater;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.rx1226.appupdater.Local.Version;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.github.rx1226.appupdater.Local.ErrorMseeage.APP_PAGE_ERROR;
import static com.github.rx1226.appupdater.Local.ErrorMseeage.CONNECT_ERROR;
import static com.github.rx1226.appupdater.Local.ErrorMseeage.NETWORK_NOT_AVAILABLE;
import static com.github.rx1226.appupdater.Local.ErrorMseeage.PARSE_ERROR;
import static com.github.rx1226.appupdater.Local.Language.EN;
import static com.github.rx1226.appupdater.Local.Language.TW;
import static com.github.rx1226.appupdater.Local.Language.ZH;


public class AppUpdater {
    private static final String TAG = "AppUpdater";
    private static final String PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=%s&hl=%s";
    private static final String PLAY_STORE_TAG_RELEASE = "data-node-index=\"0;0\" jsmodel=\"hc6Ubd\"><div class=\"W4P4ne \"><div class=\"wSaTQd\">";
    private static final String PLAY_STORE_TAG_CHANGES = "data-node-index=\"7;0\" jsmodel=\"hc6Ubd\"><div class=\"W4P4ne \"><div class=\"wSaTQd\">";

    public interface OnFinishListener {
        void onSuccess(Update update);
        void onFailed(String errorMessage);
    }

    public static void getLatestAppVersion(@NonNull Context context, @NonNull final OnFinishListener listener) {
        if (!isNetworkAvailable(context)) {
            listener.onFailed(NETWORK_NOT_AVAILABLE);
        }else {
            final URL updateUrl = getUpdateURL(context);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(updateUrl).build();
            Log.d(TAG, "updateUrl = " + updateUrl);
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    listener.onFailed(CONNECT_ERROR);
                }
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response){
                    ResponseBody body = response.body();
                    try {
                        if(body != null) {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(body.byteStream(), "UTF-8"));
                            String versionString = "";
                            String changesString = "";
                            String line;
                            while ((line = reader.readLine()) != null) {
                                if (line.contains(PLAY_STORE_TAG_RELEASE)) {
                                    versionString = line;
                                } else if (line.contains(PLAY_STORE_TAG_CHANGES)) {
                                    changesString = line;
                                }
                            }
                            reader.close();
                            body.close();
                            listener.onSuccess(new Update(getVersion(versionString), getRecentChanges(changesString), updateUrl));
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        listener.onFailed(APP_PAGE_ERROR);
                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onFailed(PARSE_ERROR);
                    }
                }
            });
        }
    }

    private static URL getUpdateURL(Context context) {
        String res = String.format(PLAY_STORE_URL, context.getPackageName(), Locale.getDefault().getLanguage());
        try {
            return new URL(res);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }
    private static String getVersionString() {
        switch (Locale.getDefault().getLanguage()){
            case TW: return Version.TW;
            case ZH: return Version.ZH;
            default:
            case EN: return Version.EN;
        }
    }
    private static String getVersion(String source) {
        String pattern = "<div class=\"BgcNfc\">"+getVersionString()+"</div><span class=\"htlgb\"><div><span class=\"htlgb\">([^<]+)</span></div></span></div>";
        Matcher matcher = Pattern.compile(pattern).matcher(source);
        if (matcher.find()) return matcher.group(1);
        else return "";
    }

    private static String getRecentChanges(String source) {
        String pattern = "<content>([^\"]*)</content>";
        Matcher matcher = Pattern.compile(pattern).matcher(source);
        if (matcher.find()) return matcher.group(1);
        else return "";
    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return  (networkInfo != null && networkInfo.isConnected());
        }
        return false;
    }
}
