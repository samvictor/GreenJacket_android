package com.greenjacket.greenjacket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Size extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_size);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Context size_context = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_checkout = new Intent(size_context, Checkout.class);
                startActivity(to_checkout);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void SizeRegularButton(View view) {
        // Do something in response to button
        Intent to_options = new Intent(this, Options.class);

        switch(view.getId())
        {
            case -1: // burger
                to_options.putExtra("size", "regular");
                break;
        }

        to_options.putExtra("item_added", true);
        startActivity(to_options);
    }
}
