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

import org.json.JSONObject;

public class Container extends AppCompatActivity {
    public Boolean demo;
    public MainActivity main_activity;
    public JSONObject menu_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        main_activity = MainActivity.instance;
        menu_data = main_activity.menu_data;
        demo = main_activity.demo;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Container");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Context container_context = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Are you ready to check out?", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();
                Intent to_checkout = new Intent(container_context, Checkout.class);
                startActivity(to_checkout);

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (!demo) {
            Intent intent = getIntent();
            Log.d("Container", "from intent " + intent.getStringExtra("main_opt_id"));
            Log.d("Container", "from intent " + intent.getStringExtra("main_opt_name"));
        }
    }

    public void ContainerBurgerButton(View view) {
        // Do something in response to button
        Intent to_size = new Intent(this, Size.class);

        switch(view.getId())
        {
            case -1: // burger
                to_size.putExtra("container", "burger");
                break;
        }

        to_size.putExtra("item_added", true);
        startActivity(to_size);
    }
}
