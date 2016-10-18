package com.rafalzajfert.permissiontools.sample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainFragmentActivity extends AppCompatActivity {

    public static final String EXTRA_SUPPORT = "support";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null){
            boolean support = getIntent().getBooleanExtra(EXTRA_SUPPORT, false);
            if (support){
                getSupportFragmentManager().beginTransaction().add(R.id.fragment, MainSupportFragment.newInstance()).commit();
            }else {
                getFragmentManager().beginTransaction().add(R.id.fragment, MainFragment.newInstance()).commit();
            }
        }
    }

}
