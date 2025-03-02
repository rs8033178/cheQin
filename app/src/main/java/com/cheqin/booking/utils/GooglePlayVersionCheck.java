package com.cheqin.booking.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.cheqin.booking.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import androidx.annotation.Nullable;

public class GooglePlayVersionCheck extends AsyncTask<String, Void, String> {

    String newVersion = "";
    String currentVersion = "";
    GoolgeplayVersionListener mWsCallerVersionListener;
    boolean isVersionAvailabel;
    boolean isAvailableInPlayStore;
    Context mContext;
    String mStringCheckUpdate = "";

    public GooglePlayVersionCheck(Context mContext, GoolgeplayVersionListener callback) {
        mWsCallerVersionListener = callback;
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            isAvailableInPlayStore = true;
            if (isNetworkAvailable(mContext)) {
                mStringCheckUpdate=   getPlayStoreAppVersion("https://play.google.com/store/apps/details?id=" + mContext.getPackageName());
//                mStringCheckUpdate = Jsoup.connect("https://play.google.com/store/apps/details?id=" + mContext.getPackageName())
//                        .timeout(10000)
//                        .get()
//                        .select(".hAyfc .htlgb")
//                        .get(6)
//                        .ownText();
                Log.e(" Version","VERSIONCHECK-"+mStringCheckUpdate);
                return mStringCheckUpdate;
            }

        } catch (Exception e) {
            isAvailableInPlayStore = false;
            return mStringCheckUpdate;
        } catch (Throwable e) {
            isAvailableInPlayStore = false;
            return mStringCheckUpdate;
        }
        return mStringCheckUpdate;
    }

    @Override
    protected void onPostExecute(String string) {
        if (isAvailableInPlayStore == true) {
            newVersion = string;
            Log.e("new Version", newVersion);
            checkApplicationCurrentVersion();
            if (currentVersion.equalsIgnoreCase(newVersion)) {
                isVersionAvailabel = false;
               // Toast.makeText(mContext,"App uptodate", Toast.LENGTH_LONG).show();
            } else {
                isVersionAvailabel = true;
            }

        }
        mWsCallerVersionListener.onGetResponse(isVersionAvailabel);
    }

    /**
     * Method to check current app version
     */
    public void checkApplicationCurrentVersion() {
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        currentVersion = packageInfo.versionName;
        Log.e("currentVersion", currentVersion);
    }

    /**
     * Method to check internet connection
     * @param context
     * @return
     */
    public boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }



        @Nullable
        private static String getPlayStoreAppVersion(String appUrlString) {
            String
                    currentVersion_PatternSeq = "<div[^>]*?>Current\\sVersion</div><span[^>]*?>(.*?)><div[^>]*?>(.*?)><span[^>]*?>(.*?)</span>",
                    appVersion_PatternSeq = "htlgb\">([^<]*)</s";
            try {
                URLConnection connection = new URL(appUrlString).openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder sourceCode = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) sourceCode.append(line);

                    // Get the current version pattern sequence
                    String versionString = getAppVersion(currentVersion_PatternSeq, sourceCode.toString());
                    if (versionString == null) return null;

                    // get version from "htlgb">X.X.X</span>
                    return getAppVersion(appVersion_PatternSeq, versionString);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Nullable
        private static String getAppVersion(String patternString, String input) {
            try {
                Pattern pattern = Pattern.compile(patternString);
                if (pattern == null) return null;
                Matcher matcher = pattern.matcher(input);
                if (matcher.find()) return matcher.group(1);
            } catch (PatternSyntaxException e) {
                e.printStackTrace();
            }
            return null;
        }


    }