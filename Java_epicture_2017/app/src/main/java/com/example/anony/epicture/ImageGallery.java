package com.example.anony.epicture;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by anony on 08/02/2018.
 */

class ImageGallery implements Parcelable {

    private String              url_image;
    private String              title_image;
    private ArrayList<String[]> mGallery;
    private ImageGallery[]      gallery;

    public ImageGallery()
    {

    }

    public ImageGallery(String title, String url)
    {
        url_image = url;
        title_image = title;
    }

    protected ImageGallery(Parcel parcel) {
        url_image = parcel.readString();
        title_image = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url_image);
        dest.writeString(title_image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageGallery> CREATOR = new Creator<ImageGallery>() {
        @Override
        public ImageGallery createFromParcel(Parcel in) {
            return new ImageGallery(in);
        }

        @Override
        public ImageGallery[] newArray(int size) {
            return new ImageGallery[size];
        }
    };

    /**
     * function to get the URL of the picture
     * @return
     */
    public String getUrl_image() {
        return url_image;
    }

    /**
     * function to get the TITLE of the image
     * @return
     */
    public String getTitle_image() {
        return title_image;
    }

    /**
     * function to set the image gallery
     * @param json
     * @param type
     */
    public void setImageGallery(String json, String type)
    {
        mGallery = new ArrayList<>();
        if (type.compareTo("account") == 0)
            parseJsonGalleryAccount(json);
        else if (type.compareTo("page") == 0)
            parseJsonGallery(json);
        else
            return ;
        parseJsonGallery(json);
        gallery = new ImageGallery[mGallery.size()];
        for (int i=0; i < mGallery.size(); i++)
        {
            String[] item = mGallery.get(i);
            gallery[i] = new ImageGallery(item[0], item[1]);
            System.out.println("gallery " + i + " : title => " + gallery[i].getTitle_image() +
                    " , url => " + gallery[i].getUrl_image());
        }
    }

    /**
     * function to set the image gallery
     * @return
     */
    public ImageGallery[] getImageGallery() {
        return (gallery);
    }

    /**
     * function to parse the JsonGallery to extract the useful information
     * @param jsonData
     */
    private void parseJsonGallery(String jsonData) {
        String title = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if (!jsonObject.isNull("data"))
            {
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i=0; i < data.length(); i++)
                {
                    JSONObject item = (JSONObject) data.get(i);
                    if (!item.isNull("images"))
                    {
                        JSONArray images = item.getJSONArray("images");
                        for (int j=0; j < images.length(); j++)
                        {
                            JSONObject image = (JSONObject)images.get(j);
                            if (!image.isNull("id")) {
                                title = image.getString("id");
                            }
                            if (!image.isNull("link")) {
                                String[] itemGallery = {title, image.getString("link")};
                                mGallery.add(itemGallery);
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * function to extract and parse the Json data.
     * @param jsonData
     */
    private void parseJsonGalleryAccount(String jsonData) {
        String title = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if (!jsonObject.isNull("data"))
            {
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i=0; i < data.length(); i++)
                {
                    JSONObject item = (JSONObject) data.get(i);
                    if (!item.isNull("id")) {
                        title = item.getString("id");
                    }
                    if (!item.isNull("link"))
                    {
                        String[] itemGallery = {title, item.getString("link")};
                        mGallery.add(itemGallery);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
