package com.btech.funnyphoto;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by mrtran on 2018/09/18.
 */

public class AdmodData {
    private Context context;
    private InterstitialAd interstitialAd;

    public AdmodData() {
        getAdmodId();
    }

    public AdmodData(Context context) {
        this.context = context;
        createAdmod();
    }

    private void getAdmodId() {
        new GetContacts().execute();
    }

    private void createAdmod() {
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(Admod.interstial_ap_id);
        // Create ad request.
        AdRequest adRequest = new AdRequest.Builder().build();
        // Begin loading your interstitial.
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                interstitialAd.show();
            }
        });
        interstitialAd.loadAd(adRequest);

    }

    public class HttpHandler {

        private final String TAG = HttpHandler.class.getSimpleName();

        public HttpHandler() {
        }

        public String makeServiceCall(String reqUrl) {
            String response = null;
            try {
                URL url = new URL(reqUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                response = convertStreamToString(in);
            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (ProtocolException e) {
                Log.e(TAG, "ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
            return response;
        }

        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return sb.toString();
        }


    }

    public static class Admod {
        public static String banner_ap_id = "";
        public static String interstial_ap_id = "ca-app-pub-8877440675991474/4897732290";
    }

    public class GetContacts extends AsyncTask<Void, Void, Admod> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Admod doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://demo9614393.mockable.io/admod";
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);
            Admod admod = new Admod();
            if (jsonStr == null) {
                return admod;
            }
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject contacts = jsonObj.optJSONObject("admod");
                Admod.banner_ap_id = contacts.optString("banner_ap_id");
                Admod.interstial_ap_id = contacts.optString("interstial_ap_id");

            } catch (JSONException e) {
                e.printStackTrace();
                return admod;
            }
            return admod;
        }

        @Override
        protected void onPostExecute(Admod admod) {
            super.onPostExecute(admod);
        }
    }
}
