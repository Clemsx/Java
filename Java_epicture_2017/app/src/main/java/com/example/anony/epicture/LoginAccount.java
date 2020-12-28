package com.example.anony.epicture;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anony.epicture.db.TaskDbHelper;

/**
 * Created by anony on 08/02/2018.
 */

public class LoginAccount extends Fragment {

    private View                                view;
    private Button                              connect;
    private WebView                             authWebView;
    private EditText                            id_client;
    private ImageView                           imgur_logo;
    private TextView                            label_imgur;
    private Imgur                               imgurAPI;
    private FragmentTransaction                 fragmentTransaction;
    private FragmentManager                     fragmentManager;
    private com.example.anony.epicture.Gallery  fGallery;
    private TaskDbHelper                        mHelper;
    private String                              idClient = "4c87c24c218e973";

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mHelper = new TaskDbHelper(getActivity());
        view = inflater.inflate(R.layout.login, container, false);
        initElement();
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgur_logo.setVisibility(View.INVISIBLE);
                label_imgur.setVisibility(View.INVISIBLE);
                connect.setVisibility(View.INVISIBLE);
                id_client.setVisibility(View.INVISIBLE);
                authWebView.setVisibility(View.VISIBLE);
                authWebView.loadUrl((imgurAPI.getAuthUrl(idClient)));
                authWebView.getSettings().setJavaScriptEnabled(true);
                authWebView.setWebViewClient(new WebViewClient() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView webView, String url)
                    {
                        if (!url.isEmpty()) {
                            if (imgurAPI.parseAuthUrl(url, webView)) {
                                Bundle args = new Bundle();
                                args.putParcelable("imgurapi", imgurAPI);
                                fGallery.setArguments(args);
                                fragmentTransaction.replace(R.id.activity_main, fGallery);
                                fragmentTransaction.commit();
                            }
                        }
                        else
                            webView.loadUrl(url);
                        return (true);
                    }
                });
            }
        });
        return (view);
    }

    private void initElement()
    {
        connect = view.findViewById(R.id.connect_account);
        authWebView = view.findViewById(R.id.auth_webview);
        authWebView.setBackgroundColor(Color.TRANSPARENT);
        label_imgur = view.findViewById(R.id.label_imgur);
        imgur_logo = view.findViewById(R.id.imgur_logo);
        id_client = view.findViewById(R.id.id_client);
        imgurAPI = new Imgur();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fGallery = new com.example.anony.epicture.Gallery();
    }

}