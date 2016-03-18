package com.greenjacket.greenjacket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainIngredient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // Recieve data from category
        Intent received_intent = getIntent();
        System.out.println("after intent:");
        System.out.println(received_intent.getStringExtra("category_id"));
        System.out.println(received_intent.getStringExtra("category_name"));
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
