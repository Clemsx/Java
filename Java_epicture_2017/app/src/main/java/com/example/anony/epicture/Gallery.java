package com.example.anony.epicture;

/**
 * Created by anony on 08/02/2018.
 */

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class Gallery extends Fragment {

    private View                view;
    private ListView            viewGallery;
    private List<ImageGallery>  listGallery;
    private GalleryAdapter      galleryAdapter;
    private ImageGallery        imageGallery;
    private Imgur               imgurAPI;
    private EditText            search_field;
    private ProgressBar         progressBar;
    private ImageButton         uploadPicture;
    private ImageButton         favoritePicture;
    private ImageButton         accountPicture;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.image_gallery, container, false);
        viewGallery = view.findViewById(R.id.listGallery);
        listGallery = new ArrayList<>();
        galleryAdapter = new GalleryAdapter(getActivity(), R.id.listGallery, listGallery);
        progressBar = view.findViewById(R.id.progressBar);
        imageGallery = new ImageGallery();

        List<String> list = new ArrayList<>();
        list.add("time");
        list.add("viral");
        list.add("top");
        ArrayAdapter<String> dataFilter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list);
        dataFilter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        activeProgressBar(false);
        if (getArguments() != null)
        {
            imgurAPI = getArguments().getParcelable("imgurapi");
            if (imgurAPI != null)
            {
                String jsonData = imgurAPI.getImageGalleryAccount(getActivity());
                imageGallery.setImageGallery(jsonData, "account");
                Log.i("imageGallery", Integer.toString(imageGallery.getImageGallery().length));
                setGallery();
            }
        }
        search_field = view.findViewById(R.id.search);
        search_field.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Log.i("search", search_field.getText().toString());
                    hideSoftKeyboard(getActivity());
                    activeProgressBar(true);
                    String jsonData = imgurAPI.getImageFromSearch(search_field.getText().toString(),
                            "", "day", "1", getActivity());
                    imageGallery.setImageGallery(jsonData, "page");
                    setGallery();
                    activeProgressBar(false);
                    return (true);
                }
                return false;
            }
        });
        uploadPicture = view.findViewById(R.id.uploadPicture);
        uploadPicture.bringToFront();
        uploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(i, "Select picture"), 0);
            }
        });
        favoritePicture = view.findViewById(R.id.favorite);
        favoritePicture.bringToFront();
        favoritePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jsonData = imgurAPI.getImageGalleryFavorite(getActivity());
                imageGallery.setImageGallery(jsonData, "account");
                Log.i("imageGallery", Integer.toString(imageGallery.getImageGallery().length));
                setGallery();
            }
        });
        accountPicture = view.findViewById(R.id.account);
        accountPicture.bringToFront();
        accountPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jsonData = imgurAPI.getImageGalleryAccount(getActivity());
                imageGallery.setImageGallery(jsonData, "account");
                Log.i("imageGallery", Integer.toString(imageGallery.getImageGallery().length));
                setGallery();
            }
        });
        viewGallery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("image click", listGallery.get(i).getTitle_image());
                if (imgurAPI.setFavoriteImage(listGallery.get(i).getTitle_image(), getActivity()))
                    Toast.makeText(getActivity(), "Image added to favorites successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "Failed to add image to favorites", Toast.LENGTH_SHORT).show();
                return (true);
            }
        });
        return (view);
    }

    /**
     * setGallery set the gallery
     */
    private void setGallery()
    {
        cleanCache();
        new Thread() {
            public void run()
            {
                if (getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageGallery[] gallery = imageGallery.getImageGallery();
                        listGallery.clear();
                        viewGallery.setAdapter(galleryAdapter);
                        for (int i = 0; i < gallery.length; i++)
                            listGallery.add(gallery[i]);
                        viewGallery.setAdapter(galleryAdapter);
                    }
                });
            }
        }.start();
    }

    private static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void activeProgressBar(final boolean active)
    {
        new Thread()
        {
            public void run()
            {
                if (getActivity() == null)
                    return ;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.isIndeterminate();
                        if (!active)
                            progressBar.setVisibility(View.GONE);
                        else
                            progressBar.setVisibility(View.VISIBLE);
                    }
                });
            }
        }.start();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            String path = getPathFromCameraData(data, this.getActivity());
            Log.i("PICTURE", "Path: " + path);
            if (path != null) {
                if (ContextCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.i("permission", "granted");
                    if (imgurAPI.uploadImage(path, "TestUpload"))
                        Toast.makeText(getActivity(), "Upload picture successful", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getActivity(), "Upload picture failure", Toast.LENGTH_SHORT).show();
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), READ_EXTERNAL_STORAGE)) {
                    Log.i("permission", "ask");
                } else {
                    Log.i("permission", "never asked");
                    this.requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 1);
                }
            }
        }
    }

    private static String getPathFromCameraData(Intent data, Context context) {
        int columnIndex = 0;
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        try {
            cursor.moveToFirst();
            columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String tmp : children) {
                boolean success = deleteDir(new File(dir, tmp));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else
            return (dir != null && dir.isFile() && dir.delete());
    }

    private void cleanCache()
    {
        try {
            File dir = getActivity().getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
