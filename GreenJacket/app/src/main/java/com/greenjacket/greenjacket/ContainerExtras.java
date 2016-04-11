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
 * Created by Sammy Sam on 3/26/2016.
 */
public class ContainerExtras {
    Container container_activity;
    Context container_context;

    public ContainerExtras (Context new_context, Container new_container_activity) {
        //setContentView(R.layout.activity_main);
        container_activity = new_container_activity;
        container_context = new_context;
    }


    // Send Data to Size
    public void ContainerButton(View view)
    {
        // Do something in response to button
        Intent to_size = new Intent(container_context, Size.class);

        to_size.putExtra("category_id", container_activity.category_id);
        to_size.putExtra("category_name", container_activity.category_name);
        to_size.putExtra("main_opt_id", container_activity.main_opt_id);
        to_size.putExtra("main_opt_name", container_activity.main_opt_name);
        to_size.putExtra("container_id", view.getTag(R.string.button_id_tag).toString());
        to_size.putExtra("container_name", view.getTag(R.string.button_name_tag).toString());

        container_activity.startActivity(to_size);
    }



    public void CreateContainerButtonsDo(JSONObject containers) throws JSONException
    {
        ArrayList<String> container_names = new ArrayList<String>();
        ArrayList <String> container_ids = new ArrayList<String>();

        for(Iterator<String> container_iter = containers.keys(); container_iter.hasNext();) {
            String key = container_iter.next();
            String new_name = containers.getJSONObject(key).getString("name");
            //System.out.println(new_name);
            container_names.add(new_name);
            container_ids.add(key);
        }

        View.OnClickListener container_listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ContainerButton(v);
            }
        };

        //GridLayout grid_layout_include = (GridLayout) container_activity.findViewById(R.id.content_container_include);
        GridLayout grid_layout = (GridLayout) container_activity.findViewById(R.id.container_content);

        CategoryExtras.CreateButtons(container_ids, container_names, container_listener,
                "container_", grid_layout, container_context,
                container_activity);
    }
}
