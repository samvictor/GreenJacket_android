package com.greenjacket.greenjacket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

/**
 * Created by Sammy Sam on 3/17/2016.
 */
public class CategoryExtras {
    public String test = "test";
    public JSONObject menu_data;
    public Context main_context;
    public MainActivity main_activity;

    public CategoryExtras (Context new_context, MainActivity new_main_activity) {
        //setContentView(R.layout.activity_main);
        main_activity = new_main_activity;
        main_context = new_context;
    }

    public void CategoryButton(View view)
    {
        // Do something in response to button
        Intent to_main_ing = new Intent(main_context, MainIngredient.class);

        to_main_ing.putExtra("category_id", view.getTag(R.string.button_id_tag).toString());
        to_main_ing.putExtra("category_name", view.getTag(R.string.button_name_tag).toString());

        main_activity.startActivity(to_main_ing);
    }

    public void CreateCategoryButtons(JSONObject in_menu_data) throws JSONException
    {
        menu_data = in_menu_data;
        CreateCategoryButtons();
    }

    // optional argument add_to_front
    public String CleanForImage(String raw_string)
    {
        // create a new copy
        String out_str = new String(raw_string);
        out_str = out_str.toLowerCase();
        out_str = out_str.replace(" ", "_");
        out_str = out_str.replaceAll("[^a-z|_|0-9]+", "");

        return out_str;
    }

    public void CreateCategoryButtons () throws JSONException
    {

        JSONObject categories = menu_data.getJSONObject("categories");
        //System.out.println(menu_data.getJSONObject("categories"));

        ArrayList<String> cat_names = new ArrayList<String>();
        ArrayList <String> cat_ids = new ArrayList<String>();

        for(Iterator<String> cat_iter = categories.keys(); cat_iter.hasNext();) {
            String key = cat_iter.next();
            String new_name = categories.getJSONObject(key).getString("name");
            //System.out.println(new_name);
            cat_names.add(new_name);
            cat_ids.add(key);
        }

        View.OnClickListener cat_listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CategoryButton(v);
            }
        };

        GridLayout gridLayout = (GridLayout) main_activity.findViewById(R.id.main_content);
        gridLayout.removeAllViews();
        int total = cat_names.size();
        int column = 3;
        int row = total / column;
        gridLayout.setColumnCount(column);
        gridLayout.setRowCount((row + 1) * 2);
        for (int i = 0, c = 0, r = 0; i < total; i++, c++) {
            if (c == column) {
                c = 0;
                r++;
            }
            ImageButton new_button = new ImageButton(main_context);

            try{
                new_button.setImageResource(R.mipmap.class.getField("category_" + CleanForImage(cat_names.get(i))).getInt("id"));
            }
            catch (Exception e)
            {
                Log.w("buttonbuilder", "Failed to get image with name: " + "category_" + CleanForImage(cat_names.get(i)));
                new_button.setImageResource(R.mipmap.sandwich);
            }
            // 0 is id, 1 is name
            new_button.setTag(R.string.button_id_tag, cat_ids.get(i));
            new_button.setTag(R.string.button_name_tag, cat_names.get(i));
            new_button.setScaleType(ImageView.ScaleType.FIT_CENTER);
            new_button.setOnClickListener(cat_listener);

            GridLayout.LayoutParams b_param = new GridLayout.LayoutParams();
            b_param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            b_param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            b_param.rightMargin = 5;
            b_param.topMargin = 5;
            b_param.setGravity(Gravity.CENTER);

            try {
                b_param.columnSpec = GridLayout.spec(c, 1f);
            }
            catch (NoSuchMethodError e)
            {
                b_param.columnSpec = GridLayout.spec(c);
            }
            b_param.rowSpec = GridLayout.spec(r*2);
            new_button.setLayoutParams(b_param);
            gridLayout.addView(new_button);

            TextView new_text = new TextView(main_context);
            new_text.setText(cat_names.get(i));
            new_text.setTextColor(Color.parseColor("#ffffff"));
            new_text.setGravity(Gravity.CENTER);
            GridLayout.LayoutParams t_param = new GridLayout.LayoutParams();
            t_param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            t_param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            t_param.rightMargin = 5;
            t_param.topMargin = 5;

            try {
                t_param.columnSpec = GridLayout.spec(c, 1f);
            }
            catch (NoSuchMethodError e)
            {
                t_param.columnSpec = GridLayout.spec(c);
            }
            t_param.rowSpec = GridLayout.spec(r*2+1);
            new_text.setLayoutParams(t_param);
            gridLayout.addView(new_text);
        }
    }

    private final String LogTag = "Category Extras";

    public String DownloadMenuDo(String url_base)
    {
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

    public JSONObject DownloadMenuPost(String result) {
        // What to do with menu?
        Log.d(LogTag, "result is " + result);
        String menu_string = result;

        try {
            menu_data = new JSONObject(menu_string);
            Log.d(LogTag, "menu_data is " + menu_data.toString());
            CreateCategoryButtons();
        }
        catch (JSONException e)
        {
            Log.e(LogTag, "Problem creating/querying json data. " + e);
        }

        return menu_data;
    }
}
