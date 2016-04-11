package com.greenjacket.greenjacket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class Home extends AppCompatActivity {

    public Context home_context;
    public MainActivity main_activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        main_activity = MainActivity.instance;

        if (main_activity == null) {
            Log.d("Home", "main was null, going to cat");

            Intent to_category = new Intent(this, MainActivity.class);
            to_category.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            to_category.putExtra("data_lost", true);
            startActivity(to_category);
            finish();
            return;
        }

        home_context = this;
        main_activity.go_home = false;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_activity.loading_data = true; // home needs to be it's own activity with qr scanner
                main_activity.go_home = false;

                Intent to_category = new Intent(home_context, MainActivity.class);
                to_category.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //to_category.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                to_category.putExtra("start_qr", true);

                startActivity(to_category);
                Log.d("Home", "on my way to main");
            }
        });
    }

}
