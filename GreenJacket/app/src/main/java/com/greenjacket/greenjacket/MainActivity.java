package com.greenjacket.greenjacket;

import android.app.ActionBar;
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
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public String menu_data = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start downloading menu data
        new DownloadMenu().execute();

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

    // Get data from website
    private class DownloadMenu extends AsyncTask<String, Void, String> {
        private final String LogTag = DownloadMenu.class.getSimpleName();
        //private final String url_base = "http://localhost:8000";
        private final String url_base = "http://10.0.2.2:8000"; // use this for localhost in emulator

        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                URL url = new URL(url_base + "/GJ_app/data/customer/menu/?branch=1");

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();

                int response = urlConnection.getResponseCode();
                Log.d(LogTag, "Response for menu: " + response);

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) {
                    Log.e(LogTag, "url gave empty response");
                    return "";
                }

                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    Log.e(LogTag, "buffer was empty");
                    return "";
                }

                String ret_string = buffer.toString();
                return ret_string;
            } catch (IOException e) {

                Log.e(LogTag, "Error ", e);
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            // What to do with menu?
            Log.d(LogTag, "result is " + result);
            menu_data = result;
        }
    }


    // Function for category buttons
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


