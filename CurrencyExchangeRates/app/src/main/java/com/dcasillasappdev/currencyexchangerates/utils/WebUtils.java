package com.dcasillasappdev.currencyexchangerates.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebUtils {

    private static final String TAG = Utils.class.getSimpleName();

    /* Get historical rates for any day since 1999.
     * http://api.fixer.io/2000-01-03 */

    public static String getRateByDate(String date, String base) {
        return Constants.URL + date + "?" + Constants.BASE + "=" + base;
    }

     /* Rates are quoted against the Euro by default.
     * Quote against a different currency by setting the base parameter in your request.
     * http://api.fixer.io/latest?base=USD */

    public static String getLatestRate(String base) {
        return Constants.URL + Constants.LATEST + "?" + Constants.BASE + "=" + base;
    }

    public static JSONObject getJSONObject(String webUrl) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(webUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(Constants.READ_TIMEOUT);

            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.d(TAG, "Unauthorized access!");
            } else if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                Log.d(TAG, "404 page not found");
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "URL Response error");
            }

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String response;

            while ((response = bufferedReader.readLine()) != null) {
                sb.append(response);
            }

            return new JSONObject(sb.toString());

        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager != null &&
                connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }
}