package com.greenjacket.greenjacket;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Options extends AppCompatActivity {
    public Boolean demo;
    public boolean meals;

    public MainActivity main_activity;
    public JSONObject menu_data;

    public Context options_context;
    public JSONObject options_data;
    private OptionsExtras extras;

    public String category_id;
    public String category_name;
    public String main_opt_id;
    public String main_opt_name;
    public String container_id;
    public String container_name;
    public String size_id;
    public String size_name;
    public String size_price;
    public ArrayList<String> chosen_option_ids; // list of option ids
    public ArrayList<String> chosen_option_names;
    public ArrayList<String> chosen_option_prices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        main_activity = MainActivity.instance;

        if (main_activity == null) {
            Log.d("Options", "main was null, going to cat");

            Intent to_category = new Intent(this, MainActivity.class);
            to_category.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            to_category.putExtra("data_lost", true);
            startActivity(to_category);
            finish();
            return;
        }

        menu_data = main_activity.menu_data;
        demo = main_activity.demo;
        meals = main_activity.meals;

        options_context = this;
        extras = new OptionsExtras(options_context, this);
        chosen_option_ids = new ArrayList<String>();
        chosen_option_names = new ArrayList<String>();
        chosen_option_prices = new ArrayList<String>();


        if (!demo)
        {
            Intent intent = getIntent();
            category_id = intent.getStringExtra("category_id");
            category_name = intent.getStringExtra("category_name");
            main_opt_id = intent.getStringExtra("main_opt_id");
            main_opt_name = intent.getStringExtra("main_opt_name");
            container_id = intent.getStringExtra("container_id");
            container_name = intent.getStringExtra("container_name");
            size_id = intent.getStringExtra("size_id");
            size_name = intent.getStringExtra("size_name");
            size_price = intent.getStringExtra("size_price");

            try {
                options_data = menu_data.getJSONObject("categories").getJSONObject(category_id).getJSONObject("mains"
                ).getJSONObject(main_opt_id).getJSONObject("containers").getJSONObject(container_id).getJSONObject("sizes"
                ).getJSONObject(size_id);
                System.out.println(options_data);
            }
            catch (JSONException e) {
                Log.e("Options", "Error getting size data: " + e.toString());
            }

            new CreateOptionsButtons().execute();

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Options");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        else
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Context options_context = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_category = new Intent(options_context, MainActivity.class);
                to_category.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //to_category.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                to_category.putExtra("item_added", true);

                if (!demo)
                {
                    to_category.putExtra("category_id", category_id);
                    to_category.putExtra("category_name", category_name);
                    to_category.putExtra("main_opt_id", main_opt_id);
                    to_category.putExtra("main_opt_name", main_opt_name);
                    to_category.putExtra("container_id", container_id);
                    to_category.putExtra("container_name", container_name);
                    to_category.putExtra("size_id", size_id);
                    to_category.putExtra("size_name", size_name);
                    to_category.putExtra("size_price", size_price);

                    to_category.putExtra("chosen_option_ids", chosen_option_ids);
                    to_category.putExtra("chosen_option_names", chosen_option_names);
                    to_category.putExtra("chosen_option_prices", chosen_option_prices);

                    try {
                        JSONObject real_item = options_data.getJSONObject("real_item");
                        to_category.putExtra("real_name", real_item.getString("name"));
                    }
                    catch (JSONException e)
                    {
                        Log.e("Options -> Category", "Error getting real item data: " + e);
                    }
                }

                startActivity(to_category);
            }
        });
    }

    // real

    private class CreateOptionsButtons extends AsyncTask<String, String, Boolean>
    {
        private final String LogTag = CreateOptionsButtons.class.getSimpleName();
        protected Boolean doInBackground(String... url)
        {
            try {
                if (meals) {
                    extras.CreateOptionsButtonsDo(options_data.getJSONObject("options"),
                            options_data.getJSONObject("meals"));
                }
                else
                {
                    extras.CreateOptionsButtonsDo(options_data.getJSONObject("options"),
                            new JSONObject("{}"));
                }
                extras.CreateOptionsDisplayDo(options_data.getJSONObject("real_item"),
                        options_data.getString("price"));
            }
            catch (JSONException e)
            {
                Log.e(LogTag, "Error getting options/real_item data: " + e.toString());
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


    // demo

    public void OptionsBaconButton(View view) {
        // Do something in response to button
        final CheckBox checkBox = (CheckBox) findViewById(R.id.bacon_checkbox);
        checkBox.toggle();
    }

    public void OptionsPicklesButton(View view) {
        // Do something in response to button
        final CheckBox checkBox = (CheckBox) findViewById(R.id.pickles_checkbox);
        checkBox.toggle();
    }

    public void FixedChosen(View view)
    {
        //this_view = this.findViewById(android.R.id.content).findViewWithTag("category fab");
        View this_view = this.findViewById(R.id.fab);
        Snackbar.make(this_view, "This item is required", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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
            Log.e("Options", "Error restoring data: " + e);
        }
    }
}
