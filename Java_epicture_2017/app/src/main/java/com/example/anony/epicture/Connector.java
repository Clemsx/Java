package com.example.anony.epicture;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by anony on 08/02/2018.
 */

/**
 * Connector class contains many information to get a connexion to the API
 */
public class Connector {
    /**
     * Connector constructor
     * @param jsonURL URL
     * @param type
     * @param connectTimeout time max to connect
     * @param readTimeout time max to read
     * @param keyParam Property request
     * @param valueParam Property
     * @return
     */
    public static Object connect(String jsonURL, String type, int connectTimeout,
                                 int readTimeout, String keyParam, String valueParam)
    {
        try {
            URL url = new URL(jsonURL);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();

            con.setRequestMethod(type);
            con.setRequestProperty(keyParam, valueParam);
            con.setConnectTimeout(connectTimeout);
            con.setReadTimeout(readTimeout);
            con.setDoInput(true);
            return (con);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ("Error " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return ("Error " + e.getMessage());
        }
    }
}
