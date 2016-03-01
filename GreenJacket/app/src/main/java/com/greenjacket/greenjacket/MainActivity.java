package com.greenjacket.greenjacket;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar toolbar_support = getSupportActionBar();
        toolbar_support.setTitle("Green Jacket");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        final Context main_context = this;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 //Snackbar.make(view, "Are you ready to check out?", Snackbar.LENGTH_LONG)
                 //      .setAction("Action", null).show();
                 Intent to_checkout = new Intent(main_context, Checkout.class);
                System.out.println("button id is" + view.getId());
                 startActivity(to_checkout);

            }
        });

        Intent received_intent = getIntent();
        if(received_intent.getBooleanExtra("item_added", false))
        {

            View this_view = this.findViewById(android.R.id.content);
            // fab id is 2131492970

            //this_view = this.findViewById(android.R.id.content).findViewWithTag("category fab");
            this_view = this.findViewById(R.id.fab);
            Snackbar.make(this_view, "Item added to cart", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void MeatButton(View view) {
        // Do something in response to button
        Intent to_container = new Intent(this, Container.class);

        switch(view.getId())
        {
            case R.id.meat_button: // Meat
                to_container.putExtra("category", "meat");
                break;
        }

        startActivity(to_container);

    }
}


