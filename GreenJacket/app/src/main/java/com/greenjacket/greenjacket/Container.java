package com.greenjacket.greenjacket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Container extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Meat");

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

        Intent intent = getIntent();
        //System.out.println("from intent " + intent.getStringExtra("category"));
    }

    public void ContainerBurgerButton(View view) {
        // Do something in response to button

        Intent to_options = new Intent(this, Options.class);

        switch(view.getId())
        {
            case -1: // burger
                to_options.putExtra("container", "burger");
                break;
        }

        to_options.putExtra("item_added", true);
        startActivity(to_options);
    }
}
