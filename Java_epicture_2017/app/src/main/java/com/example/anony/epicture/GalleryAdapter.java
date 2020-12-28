package com.example.anony.epicture;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by anony on 08/02/2018.
 */

public class GalleryAdapter extends ArrayAdapter<ImageGallery> {

    private static class ViewItemGallery {
        ImageView imageItem;
    }

    /**
     * GalleryAdapter constructor
     * @param context
     * @param resource
     * @param objects
     */
    public GalleryAdapter(Context context, int resource, List<ImageGallery> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int pos, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ImageGallery gallery = getItem(pos);
        ViewItemGallery itemGallery;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.image_item, parent, false);
            itemGallery = new ViewItemGallery();
            itemGallery.imageItem = row.findViewById(R.id.itemPicture);
            try {
                Glide.with(getContext()).load(gallery.getUrl_image()).into(itemGallery.imageItem);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            row.setTag(itemGallery);
        } else {
            itemGallery = (ViewItemGallery) row.getTag();
            try {
                Glide.with(getContext()).load(gallery.getUrl_image()).into(itemGallery.imageItem);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return (row);
    }
}
