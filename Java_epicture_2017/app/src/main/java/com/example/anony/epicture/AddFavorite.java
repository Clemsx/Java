package com.example.anony.epicture;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by anony on 08/02/2018.
 */

public class AddFavorite extends AsyncTask<Void,Void,Integer> {

    private String  url;
    private String  accessToken;

    /**
     * AddFavorite constructor taking the url of the picture and the token to authorize the access to your account
     * @param url
     * @param accessToken
     */
    public AddFavorite(String url, String accessToken)
    {
        this.url = url;
        this.accessToken = accessToken;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return (this.addFavorite());
    }

    @Override
    protected void onPostExecute(Integer requestCode)
    {
        super.onPostExecute(requestCode);
    }

    /**
     * addFavorite get the URL of the picture and send it to your profile
     * @return int code to get information about error or response
     */
    private int addFavorite()
    {
        try {
            URL urlHttps = new URL(this.url);
            HttpsURLConnection connection = (HttpsURLConnection)urlHttps.openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setDoOutput(true);
            DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
            dStream.flush();
            dStream.close();
            Log.i("Response", connection.getResponseMessage());
            return (connection.getResponseCode());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (-1);
    }
}
