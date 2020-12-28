package com.example.anony.epicture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by anony on 08/02/2018.
 */

/**
 * UploadPicture class handle the uploading of picture you want to upload on your profile
 */
public class UploadPicture extends AsyncTask<Void,Void,Integer> {

    private String  url;
    private String  path;
    private String  title;
    private String  accessToken;

    /**
     * UploadPicture constructor
     * @param path path to your picture from gallery
     * @param url of your picture
     * @param title title of your picture
     * @param accessToken token to get authorize the upload
     */
    public UploadPicture(String path, String url, String title, String accessToken)
    {
        this.path = path;
        this.url = url;
        this.title = title;
        this.accessToken = accessToken;
    }

    private byte[] getBitmapBase64(String path)
    {
        Bitmap bmp = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] b = stream.toByteArray();
        return (Base64.encodeToString(b, Base64.NO_WRAP).getBytes());
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    /**
     * Do the task in background
     * @param voids
     * @return
     */
    @Override
    protected Integer doInBackground(Void... voids) {
        return (this.upload());
    }

    @Override
    protected void onPostExecute(Integer requestCode)
    {
        super.onPostExecute(requestCode);
    }

    /**
     * upload function create a bitmap64 and upload the picture with some request
     * clean the stream before closing it
     * @return -1 in case of error
     */

    public int upload()
    {
        try {
            URL urlHttps = new URL(this.url);
            HttpsURLConnection connection = (HttpsURLConnection)urlHttps.openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setRequestProperty("type", "base64");
            connection.setRequestProperty("title", title);
            connection.setDoOutput(true);
            DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
            dStream.write(getBitmapBase64(path));
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