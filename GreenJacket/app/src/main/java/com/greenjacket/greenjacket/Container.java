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

public class Container extends AppCompatActivity {
    public Boolean demo;
    public MainActivity main_activity;
    public JSONObject menu_data;

    // not in main activity
    public Context container_context;
    private ContainerExtras extras;
    public JSONObject container_data;
    public static Context instance;

    public String category_id;
    public String category_name;
    public String main_opt_id;
    public String main_opt_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Container");

        main_activity = MainActivity.instance;

        if (main_activity == null) {
            Log.d("Container", "main was null, going to cat");

            Intent to_category = new Intent(this, MainActivity.class);
            to_category.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            to_category.putExtra("data_lost", true);
            startActivity(to_category);
            finish();
            return;
        }

        menu_data = main_activity.menu_data;
        demo = main_activity.demo;
        instance = this;
        container_context = this;
        extras = new ContainerExtras(container_context, this);

        if (!demo) {
            Intent intent = getIntent();
            category_id = intent.getStringExtra("category_id");
            category_name = intent.getStringExtra("category_name");
            main_opt_id = intent.getStringExtra("main_opt_id");
            main_opt_name = intent.getStringExtra("main_opt_name");
            //Log.d("Container", "from intent " + intent.getStringExtra("main_opt_name"));

            try {
                container_data = menu_data.getJSONObject("categories").getJSONObject(category_id).getJSONObject("mains").getJSONObject(main_opt_id);
                System.out.println(container_data);
            }
            catch (JSONException e)
            {
                Log.e("Container", "Error getting data from main option: " + e.toString());
            }

            new CreateContainerButtons().execute();
        }


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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        else
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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

    public void ContainerButton(View view)
    {
        extras.ContainerButton(view);
    }

    private class CreateContainerButtons extends AsyncTask<String, String, Boolean>
    {
        private final String LogTag = CreateContainerButtons.class.getSimpleName();
        protected Boolean doInBackground(String... url)
        {
            try {
                extras.CreateContainerButtonsDo(container_data.getJSONObject("containers"));
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
            Log.e("Container", "Error restoring data: " + e);
        }
    }
}
