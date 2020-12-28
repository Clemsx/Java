package com.example.anony.epicture;

import android.app.Application;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.webkit.WebView;

import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;

/**
 * Created by anony on 08/02/2018.
 */

public class Imgur extends Application implements Parcelable {

    public String          accessToken;
    public String          refreshToken;
    public String          accountUsername;
    public String          accountId;
    public String          idClient;
    public String          urlRequest;
    public String          jsonData;

    public Imgur()
    {

    }

    protected Imgur(Parcel in) {
        accessToken = in.readString();
        refreshToken = in.readString();
        accountUsername = in.readString();
        accountId = in.readString();
        idClient = in.readString();
        urlRequest = in.readString();
        jsonData = in.readString();
    }

    public static final Creator<Imgur> CREATOR = new Creator<Imgur>() {
        @Override
        public Imgur createFromParcel(Parcel in) {
            return new Imgur(in);
        }

        @Override
        public Imgur[] newArray(int size) {
            return new Imgur[size];
        }
    };

    public String   getAuthUrl(String id_client)
    {
        idClient = id_client;
        urlRequest = "https://api.imgur.com/oauth2/authorize?client_id=" + id_client +
                "&response_type=token&state=AUTH";
        return (urlRequest);
    }

    public boolean parseAuthUrl(String url, WebView view) {
        String[] outSplit = url.split("\\#")[1].split("\\&");

        int i = 0;
        for (String s : outSplit) {
            String[] inSplit = s.split("\\=");
            switch(i) {
                case 0: // accessToken
                    accessToken = inSplit[1];
                    break;
                case 3: // refreshToken
                    refreshToken = inSplit[1];
                    break;
                case 4: // accountUsername
                    accountUsername = inSplit[1];
                    break;
                case 5: // accountId
                    accountId = inSplit[1];
                default:
                    break;
            }
            i++;
        }
        return !accessToken.isEmpty() && !refreshToken.isEmpty() && !accountUsername.isEmpty() &&
                !accountId.isEmpty();
    }

    public String getImageGallery(String section, String sort, String window, String page, Context context)
    {
        urlRequest = "https://api.imgur.com/3/gallery/" + section + "/" + sort + "/" + window +
                "/" + page + "?showViral=%7B%7BshowViral%7D%7D&mature=%7B%7BshowMature%7D%7D&album_previews=%7B%7BalbumPreviews%7D%7D";
        Log.d("urlRequest", urlRequest);
        JSONDownloader jsonDownloader = new JSONDownloader(context, urlRequest);
        jsonDownloader.setTypeRequest("GET");
        jsonDownloader.setConnectTimeout(15000);
        jsonDownloader.setReadTimeout(15000);
        jsonDownloader.setKeyParam("Authorization");
        jsonDownloader.setValueParam("Client-ID " + idClient);
        try {
            return (jsonDownloader.execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return (null);
    }

    /**
     * get the picture from gallery by using the request to get it from imgur api
     * @param context
     * @return
     */
    public String getImageGalleryAccount(Context context)
    {
        urlRequest = "https://api.imgur.com/3/account/3p1cture/images";
        Log.d("urlRequest", urlRequest);
        JSONDownloader jsonDownloader = new JSONDownloader(context, urlRequest);
        jsonDownloader.setTypeRequest("GET");
        jsonDownloader.setConnectTimeout(15000);
        jsonDownloader.setReadTimeout(15000);
        jsonDownloader.setKeyParam("Authorization");
        jsonDownloader.setValueParam("Bearer " + accessToken);
        try {
            return (jsonDownloader.execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return (null);
    }

    /**
     * get Image from the favorite in the gallery using GET METHOD
     * @param context
     * @return null if all is good
     */
    public String getImageGalleryFavorite(Context context)
    {
        urlRequest = "https://api.imgur.com/3/account/3p1cture/favorites/0";
        Log.d("urlRequest", urlRequest);
        JSONDownloader jsonDownloader = new JSONDownloader(context, urlRequest);
        jsonDownloader.setTypeRequest("GET");
        jsonDownloader.setConnectTimeout(15000);
        jsonDownloader.setReadTimeout(15000);
        jsonDownloader.setKeyParam("Authorization");
        jsonDownloader.setValueParam("Bearer " + accessToken);
        try {
            return (jsonDownloader.execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return (null);
    }

    /**
     * find picture sorted by tag such as "viral",
     * @param keyword tag
     * @param sort sort by
     * @param window on the window
     * @param page number of page displayed
     * @param context context to download the picture
     * @return
     */
    public String getImageFromSearch(String keyword, String sort, String window, String page, Context context)
    {
        urlRequest = "https://api.imgur.com/3/gallery/search/{{" + sort + "}}/{{" +
                window + "}}/{{" + page + "}}?q=" + keyword;
        Log.d("urlRequest", urlRequest);
        JSONDownloader jsonDownloader = new JSONDownloader(context, urlRequest);
        jsonDownloader.setTypeRequest("GET");
        jsonDownloader.setConnectTimeout(15000);
        jsonDownloader.setReadTimeout(15000);
        jsonDownloader.setKeyParam("Authorization");
        jsonDownloader.setValueParam("Client-ID " + idClient);
        try {
            return (jsonDownloader.execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return (null);
    }

    /**
     * get the request code from imgur api to upload an image
     * @param path
     * @param title
     * @return
     */
    public boolean uploadImage(String path, String title)
    {
        urlRequest = "https://api.imgur.com/3/image";
        if (path == null)
            return (false);
        try {
            int requestCode = new UploadPicture(path, urlRequest, title, accessToken).execute().get();
            Log.i("RequestCode", Integer.toString(requestCode));
            return (requestCode == HttpURLConnection.HTTP_OK);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return (false);
    }

    /**
     * set in favorite the picture you want by holding 2 seconds in the button or the picture
     * @param id
     * @param contex
     * @return
     */
    public boolean setFavoriteImage(String id, Context contex)
    {
        urlRequest = "https://api.imgur.com/3/image/"+ id +"/favorite";
        Log.d("urlRequest", urlRequest);
        try {
            int requestCode = new AddFavorite(urlRequest, accessToken).execute().get();
            Log.i("RequestCode", Integer.toString(requestCode));
            return requestCode == HttpURLConnection.HTTP_OK;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return (false);
    }

    /**
     * getAccesToken
     * @return accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * getRefreshToken
     * @return refreshToken
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * getAccountUsername
     * @return accountUsername
     */
    public String getAccountUsername() {
        return accountUsername;
    }

    /**
     * getAccountId
     * @return accountId
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * getIdClient
     * @return idClient
     */
    public String getIdClient() {
        return idClient;
    }

    /**
     * describeContent
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * writeToParcel write the information in Parcel
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(accessToken);
        parcel.writeString(refreshToken);
        parcel.writeString(accountUsername);
        parcel.writeString(accountId);
        parcel.writeString(idClient);
        parcel.writeString(urlRequest);
        parcel.writeString(jsonData);
    }
}
