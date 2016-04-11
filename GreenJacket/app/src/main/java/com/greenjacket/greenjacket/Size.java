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

public class Size extends AppCompatActivity {

    public Boolean demo;
    public MainActivity main_activity;
    public JSONObject menu_data;

    // not in main activity
    public Context size_context;
    private SizeExtras extras;
    public JSONObject size_data;
    public static Context instance;

    public String category_id;
    public String category_name;
    public String main_opt_id;
    public String main_opt_name;
    public String container_id;
    public String container_name;
    public String LogTag = "Size";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_size);
        main_activity = MainActivity.instance;

        if (main_activity == null) {
            Log.d(LogTag, "main was null, going to cat");

            Intent to_category = new Intent(this, MainActivity.class);
            to_category.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            to_category.putExtra("data_lost", true);
            startActivity(to_category);
            finish();
            return;
            /*
            try { // Keep trying
                Log.d("Size", "Restarting Main Activity");
                Intent to_category = new Intent(this, MainActivity.class);
                to_category.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                to_category.putExtra("in_background", true);
                startActivity(to_category);
                main_activity = MainActivity.instance;
                Log.e("Size", "Still continuing");

                Log.d("Size", "Orders are " + savedInstanceState.getString("orders"));
                Log.d("Size", "menu data is " + savedInstanceState.getString("menu_data"));
                Log.d("Size", "main is " + main_activity);
                Log.d("Size", "Orders from main are " + main_activity.menu_data);
                main_activity.menu_data = new JSONObject(savedInstanceState.getString("menu_data"));
                main_activity.orders = new JSONArray(savedInstanceState.getString("orders"));
                System.out.println("loading saved data");
                Log.d("Size", "Orders are " + savedInstanceState.getString("orders"));
            } catch (Exception e) {
                Log.d("Size", "saved is " + savedInstanceState);
                Log.e("Size", "Error restoring data: " + e);
            }*/
        }

        menu_data = main_activity.menu_data;
        demo = main_activity.demo;

        size_context = this;
        extras = new SizeExtras(size_context, this);


        if (!demo)
        {
            Intent intent = getIntent();
            category_id = intent.getStringExtra("category_id");
            category_name = intent.getStringExtra("category_name");
            main_opt_id = intent.getStringExtra("main_opt_id");
            main_opt_name = intent.getStringExtra("main_opt_name");
            container_id = intent.getStringExtra("container_id");
            container_name = intent.getStringExtra("container_name");

            //System.out.println("my data: " + category_id + ", " + category_name + ", " + main_opt_id
            //                    + ", " + main_opt_name + ", " + container_id + ", " + container_name);

            try {
                size_data = menu_data.getJSONObject("categories").getJSONObject(category_id).getJSONObject("mains"
                            ).getJSONObject(main_opt_id).getJSONObject("containers").getJSONObject(container_id);
                //System.out.println(size_data);
            }
            catch (JSONException e)
            {
                Log.e("Size", "Error getting container data: " + e.toString());
            }

            new CreateSizeButtons().execute();
        }

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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        else
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);


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

    // Real
    public void SizeButton(View view)
    {
        extras.SizeButton(view);
    }

    private class CreateSizeButtons extends AsyncTask<String, String, Boolean>
    {
        private final String LogTag = CreateSizeButtons.class.getSimpleName();
        protected Boolean doInBackground(String... url)
        {
            try {
                extras.CreateSizeButtonsDo(size_data.getJSONObject("sizes"));
            }
            catch (JSONException e)
            {
                Log.e(LogTag, "Error getting mains: " + e.toString());
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
            Log.e("Size", "Error restoring data: " + e);
        }
    }
}
