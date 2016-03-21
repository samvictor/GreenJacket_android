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

import com.greenjacket.greenjacket.MainIngredientExtras;

import org.json.JSONException;
import org.json.JSONObject;

public class MainIngredient extends AppCompatActivity {
    // Same as main activity
    public JSONObject menu_data;
    public boolean demo;
    public MainActivity main_activity;

    // not in main activity
    public Context main_opt_context;
    public MainIngredientExtras extras;
    public JSONObject main_opt_data;
    public static  MainIngredient instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up connections to last activity
        main_opt_context = this;
        extras = new MainIngredientExtras(main_opt_context, this);
        main_activity = MainActivity.instance;
        menu_data = main_activity.menu_data;
        demo = main_activity.demo;
        instance = this;

        // Set up stuff for this activity
        setContentView(R.layout.activity_main_ingredient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Main Ingredient");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Context main_ing_context = this;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_checkout = new Intent(main_ing_context, Checkout.class);
                System.out.println("button id is" + view.getId());
                startActivity(to_checkout);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Receive data from category
        Intent received_intent = getIntent();
        System.out.println("after intent:");
        System.out.println(received_intent.getStringExtra("category_id"));
        System.out.println(received_intent.getStringExtra("category_name"));

        String category_id = received_intent.getStringExtra("category_id");
        try {
            main_opt_data = menu_data.getJSONObject("categories").getJSONObject(category_id);
            System.out.println(main_opt_data);
        }
        catch (JSONException e)
        {
            Log.e("Main Ingredient", "Error getting category data: " + e.toString());
        }

    }

    // Function for Main Ingredient buttons
    public void BurgerMeatButton(View view) {
        // Do something in response to button
        Intent to_container = new Intent(this, Container.class);

        switch(view.getId())
        {
            case R.id.meat_button: // Meat
                to_container.putExtra("main_ing", "burger_meat");
                break;
        }

        startActivity(to_container);
    }
}
