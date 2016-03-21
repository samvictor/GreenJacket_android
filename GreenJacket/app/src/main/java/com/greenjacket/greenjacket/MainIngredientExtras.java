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
 * Created by Sammy Sam on 3/21/2016.
 */
public class MainIngredientExtras {
    MainIngredient main_opt_activity;
    Context main_opt_context;

    public MainIngredientExtras (Context new_context, MainIngredient new_main_activity) {
        //setContentView(R.layout.activity_main);
        main_opt_activity = new_main_activity;
        main_opt_context = new_context;
    }

    public void MainOptButton(View view)
    {
        // Do something in response to button
        Intent to_container = new Intent(main_opt_context, Container.class);

        to_container.putExtra("main_opt_id", view.getTag(R.string.button_id_tag).toString());
        to_container.putExtra("main_opt_name", view.getTag(R.string.button_name_tag).toString());

        main_opt_activity.startActivity(to_container);
    }


    public void CreateMainOptButtonsDo(JSONObject main_opts) throws JSONException
    {
        ArrayList<String> main_opt_names = new ArrayList<String>();
        ArrayList <String> main_opt_ids = new ArrayList<String>();

        for(Iterator<String> cat_iter = main_opts.keys(); cat_iter.hasNext();) {
            String key = cat_iter.next();
            String new_name = main_opts.getJSONObject(key).getString("name");
            //System.out.println(new_name);
            main_opt_names.add(new_name);
            main_opt_ids.add(key);
        }

        View.OnClickListener main_opt_listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MainOptButton(v);
            }
        };

        GridLayout grid_layout = (GridLayout) main_opt_activity.findViewById(R.id.main_ingredient_content);

        CategoryExtras.CreateButtons(main_opt_ids, main_opt_names, main_opt_listener,
                                                "main_opt_", grid_layout, main_opt_context,
                                                main_opt_activity);
    }

}
