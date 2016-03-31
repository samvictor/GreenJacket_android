package com.greenjacket.greenjacket;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.GridLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Sammy Sam on 3/30/2016.
 */
public class SizeExtras {
    Size size_activity;
    Context size_context;

    public SizeExtras (Context new_context, Size new_size_activity) {
        //setContentView(R.layout.activity_main);
        size_activity = new_size_activity;
        size_context = new_context;
    }

    // Send Data to Options
    public void SizeButton(View view)
    {
        // Do something in response to button
        Intent to_options = new Intent(size_context, Options.class);

        to_options.putExtra("category_id", size_activity.category_id);
        to_options.putExtra("category_name", size_activity.category_name);
        to_options.putExtra("main_opt_id", size_activity.main_opt_id);
        to_options.putExtra("main_opt_name", size_activity.main_opt_name);
        to_options.putExtra("container_id", size_activity.container_id);
        to_options.putExtra("container_name", size_activity.container_name);

        to_options.putExtra("size_id", view.getTag(R.string.button_id_tag).toString());
        to_options.putExtra("size_name", view.getTag(R.string.button_name_tag).toString());

        size_activity.startActivity(to_options);
    }


    public String FormatMoney(String in_str)
    {
        String ret_str = new String(in_str);

        ret_str = ret_str.substring(0, in_str.length() - 4) +"."
                    + ret_str.substring(in_str.length() - 4, in_str.length() - 2);

        return ret_str;
    }

    public void CreateSizeButtonsDo(JSONObject sizes) throws JSONException
    {
        ArrayList<String> size_names = new ArrayList<String>();
        ArrayList <String> size_ids = new ArrayList<String>();

        ArrayList <String> size_button_names = new ArrayList<String>();
        String temp_name = "";

        for(Iterator<String> size_iter = sizes.keys(); size_iter.hasNext();) {
            String key = size_iter.next();
            String new_name = sizes.getJSONObject(key).getString("name");
            String price = sizes.getJSONObject(key).getString("price");
            String name_id = sizes.getJSONObject(key).getString("name_id");
            String count = sizes.getJSONObject(key).getString("count");
            size_ids.add(key);
            price = FormatMoney(price);
            temp_name = new_name + " $" + price;
            if (name_id == "6")
            {
                // count make sure id for count does not change
                temp_name = count+" Items\n$"+price;
            }
            size_button_names.add(temp_name);
            size_names.add(new_name);
        }

        View.OnClickListener size_listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SizeButton(v);
            }
        };

        GridLayout grid_layout = (GridLayout) size_activity.findViewById(R.id.size_content);
        /*System.out.println("size activity is " + size_activity);
        System.out.println("size_content is " + R.id.size_content);
        System.out.println("old grid is " + size_activity.findViewById(R.id.size_content));
        System.out.println("grid layout is " + grid_layout);*/

        CategoryExtras.CreateButtons(size_ids, size_names, size_listener,
                "size_", grid_layout, size_context,
                size_activity, size_button_names);
    }
}
