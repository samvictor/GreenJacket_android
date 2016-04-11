package com.greenjacket.greenjacket;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Checkout extends AppCompatActivity {
    public Boolean demo;
    public MainActivity main_activity;
    public JSONObject menu_data;

    public Context checkout_context;
    private CheckoutExtras extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Checkout");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        else
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        // get necessary things from main activity
        main_activity = MainActivity.instance;

        if (main_activity == null) {
            Log.d("Checkout", "main was null, going to cat");

            Intent to_category = new Intent(this, MainActivity.class);
            to_category.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            to_category.putExtra("data_lost", true);
            startActivity(to_category);
            finish();
            return;
        }
        demo = main_activity.demo;
        menu_data = main_activity.menu_data;

        checkout_context = this;
        extras = new CheckoutExtras(checkout_context, this);

        if (!demo)
        {
            new CreateCheckoutButtons().execute();
        }
    }

    private class CreateCheckoutButtons extends AsyncTask<String, String, Boolean>
    {
        private final String LogTag = CreateCheckoutButtons.class.getSimpleName();
        protected Boolean doInBackground(String... url)
        {
            try {
                extras.CreateCheckoutButtonsDo(main_activity.orders);
            }
            catch (JSONException e)
            {
                Log.e(LogTag, "Error creating checkout menu: " + e.toString());
            }
            return true;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Boolean success)
        {
            super.onPostExecute(true);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        try {
            savedInstanceState.putString("menu_data", main_activity.menu_data.toString());
            savedInstanceState.putString("orders", main_activity.orders.toString());
        }
        catch (Exception e)
        {
            Log.w("on save instance", "error saving data " + e);
        }
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        try {
            main_activity.menu_data = new JSONObject(savedInstanceState.getString("menu_data"));
            main_activity.orders = new JSONArray(savedInstanceState.getString("orders"));
        }
        catch (Exception e)
        {
            Log.e("Checkout", "Error restoring data: " + e);
        }
    }
}
