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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        home_context = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
