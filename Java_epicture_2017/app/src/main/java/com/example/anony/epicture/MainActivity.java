package com.example.anony.epicture;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private FragmentManager                 fragmentManager;
    private FragmentTransaction             fragmentTransaction;
    private LoginAccount                    fLoginAccount;

    /**
     * onCreate create the display of the layout and load Element
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initElement();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.add(R.id.activity_main, fLoginAccount);
        fragmentTransaction.commit();
    }

    /**
     * initialise fragment element
     */
    private void initElement()
    {
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fLoginAccount = new LoginAccount();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //call super
        Log.i("onActivityResult", "MainActivity");
    }
}
