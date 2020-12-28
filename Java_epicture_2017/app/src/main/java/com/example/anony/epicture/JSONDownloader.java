package com.example.anony.epicture;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by anony on 08/02/2018.
 */

public class JSONDownloader extends AsyncTask<Void, Void, String> {

    private Context context;
    private String      jsonURL;
    private String      typeRequest;
    private String      keyParam;
    private String      valueParam;
    private int         connectTimeout;
    private int         readTimeout;

    public JSONDownloader(Context c, String url)
    {
        context = c;
        jsonURL = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        return (this.download());
    }

    @Override
    protected void onPostExecute(final String jsonData) {
        super.onPostExecute(jsonData);
    }

    private String download()
    {
        Object connection = Connector.connect(jsonURL, typeRequest, connectTimeout,
                readTimeout, keyParam, valueParam);
        if (connection.toString().startsWith("Error")) {
            System.out.println(connection.toString());
            return (connection.toString());
        }
        try {
            HttpURLConnection con = (HttpURLConnection) connection;
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = new BufferedInputStream(con.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                String line;
                StringBuilder jsonData = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    jsonData.append(line + "\n");
                }

                br.close();
                is.close();
                System.out.println("Res : " + jsonData);
                return (jsonData.toString());
            } else {
                System.out.println(con.getResponseMessage());
                return ("Error " + con.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ("Error " + e.getMessage());
        }
    }

    public void setTypeRequest(String typeRequest) {
        this.typeRequest = typeRequest;
    }


    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }


    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setKeyParam(String keyParam) {
        this.keyParam = keyParam;
    }

    public void setValueParam(String valueParam) {
        this.valueParam = valueParam;
    }
}
