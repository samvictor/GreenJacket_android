package com.greenjacket.greenjacket;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import com.greenjacket.greenjacket.CategoryExtras;

public class MainActivity extends AppCompatActivity {
    //For the Scan QR code
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    public boolean demo = false;
    public boolean use_qr = true;
    public boolean go_home = true;

    public JSONObject menu_data = null;
    public boolean loading_data = false;
    public Context main_context;
    public CategoryExtras extras;
    public static MainActivity instance;
    public String url_args = "?branch=1";
    public String branch_id;

    public JSONArray orders;
    public ArrayList<Integer> order_ids; // don't remove


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_context = this;
        if (go_home && menu_data == null && !loading_data)
        {
            Intent to_home = new Intent(main_context, Home.class);
            startActivity(to_home);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar toolbar_support = getSupportActionBar();
        toolbar_support.setTitle("Green Jacket");

        extras = new CategoryExtras(main_context, this);
        instance = this;

        orders = new JSONArray();
        order_ids = new ArrayList<Integer>();

        if (!demo && menu_data == null && !go_home && !loading_data) {
            loading_data = true;
            new DownloadMenu().execute("");
            System.out.println("downloading menu");
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_checkout = new Intent(main_context, Checkout.class);
                startActivity(to_checkout);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        try {
            savedInstanceState.putString("menu_data", menu_data.toString());
            savedInstanceState.putString("orders", orders.toString());
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
            menu_data = new JSONObject(savedInstanceState.getString("menu_data"));
            orders = new JSONArray(savedInstanceState.getString("orders"));
        }
        catch (Exception e)
        {
            Log.e("MainActivity", "Error restoring data: " + e);
        }
    }


    @Override
    protected void onResume()
    {
        System.out.println("on resume");
        Intent received_intent = getIntent();

        if (received_intent.getBooleanExtra("start_qr", false))
        {
            scanQR(this.findViewById(R.id.qr));
            received_intent.removeExtra("start_qr");
            loading_data = true; // home needs to be it's own activity with qr scanner
        }
        else if (menu_data == null && go_home && !loading_data)
        {
            Intent to_home = new Intent(main_context, Home.class);
            startActivity(to_home);
        }

        super.onResume();
    }



    public void onNewIntent(Intent new_intent)
    {
        Log.d("Main Activity", "on new intent");
        Intent received_intent = new_intent;

        //Iterator<String > iterator = new_intent.getExtras().keySet().iterator();
        //for (;iterator.hasNext();) {
        //    System.out.println(iterator.next()); //}

        if (received_intent.getBooleanExtra("start_qr", false))
        {
            //scanQR(this.findViewById(R.id.qr));
        }
        else if (menu_data == null && go_home && !loading_data)
        {
            //Intent to_home = new Intent(main_context, Home.class);
            //startActivity(to_home);

        } else if (received_intent.getBooleanExtra("item_added", false)) {

            View this_view = this.findViewById(R.id.fab);
            Snackbar.make(this_view, "Item added to cart", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            //received_intent.removeExtra("item_added");

            JSONObject temp_order = new JSONObject();
            try {
                //temp_order.put("category_id", received_intent.getStringExtra("category_id"));
                //temp_order.put("category_name", received_intent.getStringExtra("category_name"));
                //temp_order.put("main_opt_id", received_intent.getStringExtra("main_opt_id"));
                //temp_order.put("main_opt_name", received_intent.getStringExtra("main_opt_name"));
                //temp_order.put("container_id", received_intent.getStringExtra("container_id"));
                //temp_order.put("container_name", received_intent.getStringExtra("container_name"));
                //temp_order.put("size_id", received_intent.getStringExtra("size_id"));
                //temp_order.put("size_name", received_intent.getStringExtra("size_name"));
                temp_order.put("size_price", received_intent.getStringExtra("size_price"));
                //temp_order.put("chosen_option_ids", new JSONArray(received_intent.getStringArrayListExtra("chosen_option_ids")));
                temp_order.put("chosen_option_names", new JSONArray(received_intent.getStringArrayListExtra("chosen_option_names")));
                temp_order.put("chosen_option_prices", new JSONArray(received_intent.getStringArrayListExtra("chosen_option_prices")));

                temp_order.put("real_name", received_intent.getStringExtra("real_name"));

                int max_id = 0;

                for (int i = 0; i < order_ids.size(); i++)
                {
                    max_id = Math.max(max_id, order_ids.get(i));
                }
                temp_order.put("row_id", max_id + 1);
                order_ids.add(max_id + 1);

            } catch (JSONException e) {
                Log.e("Receiving Intent", "JSON error: " + e);
            }
            orders.put(temp_order);
            System.out.println("orders are now " + orders);
        }

        setIntent(new_intent);
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

        switch(item.getItemId())
        {
            // Settings pressed
            case R.id.action_settings:
                return true;

            //case android.R.id.home:
            //    Intent up_intent = NavUtils.getParentActivityIntent(this);
            //    return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Function for meat button in demo
    public void MeatButton(View view) {
        // Do something in response to button
        Intent to_main_ing = new Intent(this, MainIngredient.class);
        to_main_ing.putExtra("category", "meat");
        startActivity(to_main_ing);
    }

    // QR Code Scanning code
    public void scanBar(View v) {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException e) {
            showDialog(MainActivity.this, "No Scanner Found",
                    "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    public void scanQR(View v) {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException e) {
            showDialog(MainActivity.this, "No QR Scanner Found",
                    "Download a QR scanner app?", "Yes", "No").show();
        }
    }

    private static AlertDialog showDialog(final AppCompatActivity act,
                                          CharSequence title, CharSequence message, CharSequence buttonYes,
                                          CharSequence buttonNo) {

        AlertDialog.Builder dowloadDialog = new AlertDialog.Builder(act);
        dowloadDialog
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(buttonYes,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Uri uri = Uri.parse("market://search?q=pname:"
                                        + "com.google.zxing.client.android");

                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        uri);
                                try {
                                    act.startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                }

                            }
                        })
                .setNegativeButton(buttonNo,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        });

        return dowloadDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == 0) {
            if(resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                /*Toast.makeText(this,
                        //"Content:" + contents + " Format:" + format,
                        "Getting menu for from company: " + contents,
                        Toast.LENGTH_LONG).show();
*/
                View this_view = this.findViewById(R.id.fab);
                Snackbar.make(this_view,
                        "Getting menu for from company: " + contents, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                url_args = "?branch="+contents;
                branch_id = contents;
                loading_data = true;
                new DownloadMenu().execute();
            }
        }
    }


    // Real

    // Get data from website
    private class DownloadMenu extends AsyncTask<String, Void, String> {
        private final String LogTag = DownloadMenu.class.getSimpleName();
        //private final String url_str = "http://localhost:8000/GJ_app/data/customer/menu/?branch=1";
        //private final String url_base = "http://10.0.2.2:8000/GJ_app/data/customer/menu/"; // use this for localhost in emulator
        private final String url_str = "http://www.saminniss.com/gh_pages_test/gj_data.json"; // online
        private final String url_base = "http://greenjacket.herokuapp.com/GJ_app/data/"; // from greenjacket

        protected String doInBackground(String... urls)
        {
            // params comes from the execute() call: params[0] is the url.
            loading_data = true;
            if (use_qr)
                return extras.DownloadMenuDo(url_base+url_args);
            else
                return extras.DownloadMenuDo(url_str);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            // What to do with menu?
            menu_data = extras.DownloadMenuPost(result);
            loading_data = false;
            super.onPostExecute(result);
        }
    }

    // Function for actual category buttons
    public void CategoryButton (View view)
    {
        extras.CategoryButton(view);
    }
}


