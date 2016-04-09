package com.greenjacket.greenjacket;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Sammy Sam on 4/4/2016.
 */
public class OptionsExtras {
    private Options options_activity;
    private Context options_context;

    OptionsExtras(Context new_context, Options new_activity)
    {
        options_activity = new_activity;
        options_context = new_context;
    }

    public void OptionsButton (View view)
    {
        String type = view.getTag(R.string.button_type_tag).toString();
        int checkbox_id = ((Integer) view.getTag(R.string.button_check_id_tag));
        String view_id = view.getTag(R.string.button_id_tag).toString();
        String view_name = view.getTag(R.string.button_name_tag).toString();
        String view_price = view.getTag(R.string.button_price_tag).toString();
        CheckBox checkbox = (CheckBox) options_activity.findViewById(checkbox_id);

        if (type.compareTo("fixed") == 0)
        {
            checkbox.toggle();
            if (checkbox != view)
                checkbox.toggle();

            View this_view = options_activity.findViewById(R.id.fab);
            Snackbar.make(this_view, "This item is required", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else if (type.compareTo("optional") == 0)
        {

            if (checkbox != view)
                checkbox.toggle();

            if (checkbox.isChecked()) {
                options_activity.chosen_option_ids.add(view_id);
                options_activity.chosen_option_names.add(view_name);
                options_activity.chosen_option_prices.add(view_price);
            }
            else{
                int option_index = options_activity.chosen_option_ids.indexOf(view_id);
                options_activity.chosen_option_ids.remove(option_index);
                options_activity.chosen_option_names.remove(option_index);
                options_activity.chosen_option_prices.remove(option_index);
            }
        }
    }

    public void CreateOptionsButtonsDo(JSONObject options, JSONObject meals) throws JSONException
    {
        ArrayList<String> options_names = new ArrayList<String>();
        ArrayList <String> options_ids = new ArrayList<String>();
        ArrayList <String> options_types = new ArrayList<String>();
        ArrayList <String> options_prices = new ArrayList<String>();
        JSONObject temp_option;


        for(Iterator<String> options_iter = options.keys(); options_iter.hasNext();) {
            String key = options_iter.next();
            temp_option = options.getJSONObject(key);

            String new_name = temp_option.getString("name");
            String price = temp_option.getString("price");
            String type = temp_option.getString("type");

            options_ids.add(key);
            options_names.add(new_name);
            options_types.add(type);
            options_prices.add(price);

            if (type.compareTo("fixed") == 0)
            {
                options_activity.chosen_option_ids.add(key);
                options_activity.chosen_option_names.add(new_name);
                //options_activity.chosen_option_prices.add(price);
                options_activity.chosen_option_prices.add("0.00");
            }
        }

        ArrayList <String> meals_opt_ids = new ArrayList<String>();
        ArrayList <String> meals_opt_names = new ArrayList<String>();
        ArrayList <String> meals_prices = new ArrayList<String>();
        ArrayList <String> meals_size_ids = new ArrayList<String>();
        ArrayList <String> meals_size_names = new ArrayList<String>();
        JSONObject temp_meal;

        for(Iterator<String> meals_iter = meals.keys(); meals_iter.hasNext();) {
            String meal_key = meals_iter.next();
            temp_meal = meals.getJSONObject(meal_key);

            String new_opt_id = temp_meal.getString("option_id");
            String new_opt_name = temp_meal.getString("option_name");
            String new_price = temp_meal.getString("price");
            String new_size_id = temp_meal.getString("size_id");
            String new_size_name = temp_meal.getString("size_name");

            meals_opt_ids.add(new_opt_id);
            meals_opt_names.add(new_opt_name);
            meals_prices.add(new_price);
            meals_size_ids.add(new_size_id);
            meals_size_names.add(new_size_name);
        }


        View.OnClickListener options_listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                OptionsButton(v);
            }
        };

        GridLayout grid_layout = (GridLayout) options_activity.findViewById(R.id.options_content_options);

        OptionsExtras.CreateOptionsButtons(options_ids, options_names, options_listener,
                "options_", grid_layout, options_context,
                options_activity, options_types, options_prices, meals_opt_ids, meals_opt_names,
                meals_prices, meals_size_ids, meals_size_names);
    }

    public void CreateOptionsDisplayDo(JSONObject real_item, String real_price) throws JSONException
    {
        String real_name = real_item.getString("name");

        final GridLayout display_grid = (GridLayout) options_activity.findViewById(R.id.options_content_display);
        display_grid.removeAllViews();
        display_grid.setColumnCount(2);
        display_grid.setRowCount(1);

        final ImageView real_image = new ImageView(options_context);
        try{
            real_image.setImageResource(R.mipmap.class.getField("real_" + CategoryExtras.CleanForImage(real_name)).getInt("id"));
        }
        catch (Exception e)
        {
            Log.w("Options Display", "Failed to get image with name: " + "real_" + CategoryExtras.CleanForImage(real_name));
            real_image.setImageResource(R.mipmap.real_big_mac);
        }
        real_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        real_image.setPadding(0, 0, 0, 0);

        GridLayout.LayoutParams b_param = new GridLayout.LayoutParams();
        b_param.height = (int) options_context.getResources().getDimension(R.dimen.options_real_image_height);
        b_param.width = (int) options_context.getResources().getDimension(R.dimen.options_real_image_width);
        b_param.setGravity(Gravity.LEFT);

        b_param.columnSpec = GridLayout.spec(0);
        b_param.rowSpec = GridLayout.spec(0);


        final TextView display_text = new TextView(options_context);
        if (real_name.length() > 20)
        {
            real_name = real_name.substring(0, 20) + "\n" + real_name.substring(20);

            if (real_name.length() > 40) {
                Log.w("Options Display", "Text " + real_name + " too long for display: truncating");
                real_name = real_name.substring(0, 40);
            }
        }
        if (real_price.length() > 7)
        {
            Log.w("Options Display", "Text "+real_price+" too long for display: truncating");
            real_price = real_price.substring(0, 7);
        }

        display_text.setText(real_name + "\n   $" + real_price);
        display_text.setTextSize(20f);
        display_text.setPadding(80, 0, 0, 0);
        display_text.setTextColor(Color.parseColor("#ffffff"));
        display_text.setGravity(Gravity.CENTER_VERTICAL);
        //display_text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        GridLayout.LayoutParams t_param = new GridLayout.LayoutParams();
        t_param.height = (int) options_context.getResources().getDimension(R.dimen.options_display_text_height);
        //t_param.width = (int) options_context.getResources().getDimension(R.dimen.options_display_text_width);
        //t_param.width = (int) options_context.getResources().getDimension(R.dimen.button_width);

        t_param.columnSpec = GridLayout.spec(1);

        t_param.rowSpec = GridLayout.spec(0);
        display_text.setLayoutParams(t_param);


        real_image.setLayoutParams(b_param); options_activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
            display_grid.addView(real_image);
            display_grid.addView(display_text);
        }
    });






    }

    // creates buttons. uses image_prefex+name to get image
    public static void CreateOptionsButtons (ArrayList<String> ids, ArrayList<String> names,
                                      View.OnClickListener listener, String image_prefix,
                                      final GridLayout gridLayout, Context context, Activity activity,
                                      final ArrayList<String> types, ArrayList<String> prices,
                                      ArrayList<String> meals_opt_ids, ArrayList<String> meals_opt_names,
                                      ArrayList<String> meals_prices, ArrayList<String> meals_size_ids,
                                      ArrayList<String> meals_size_names)
    {
        String LogTag = "createButton";
        gridLayout.removeAllViews();
        int total = names.size();
        int column = 3;
        int row = total / column;

        int meal_total = meals_opt_names.size();
        int meal_column = 3;
        int meal_row = meal_total / meal_column;

        gridLayout.setColumnCount(Math.max(column, meal_column));
        gridLayout.setRowCount((row + meal_row + 4) * 2 + 2);
        int min_total = Math.min(total, meal_total);



        // "Options:"
        final TextView options_text = new TextView(context);
        options_text.setText("Options:");
        options_text.setTextSize(20f);
        options_text.setPadding(0, 0, 0, 0);
        options_text.setTextColor(Color.parseColor("#ffffff"));
        options_text.setGravity(Gravity.LEFT);
        //display_text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        GridLayout.LayoutParams ot_param = new GridLayout.LayoutParams();
        //ot_param.height = (int) context.getResources().getDimension(R.dimen.options_display_text_height);
        ot_param.width = (int) context.getResources().getDimension(R.dimen.button_width);
        //ot_param.width = (int) context.getResources().getDimension(R.dimen.button_width);

        //ot_param.columnSpec = GridLayout.spec(0);
        ot_param.bottomMargin = 20;

        try {
            if (min_total < 3)
                ot_param.columnSpec = GridLayout.spec(0);
            else
                ot_param.columnSpec = GridLayout.spec(0, 1f);
        }
        catch (NoSuchMethodError e)
        {
            Log.w(LogTag, "Not using weight");
            ot_param.columnSpec = GridLayout.spec(0);
        }

        ot_param.rowSpec = GridLayout.spec(0);
        //ot_param.rightMargin = 20;
        //ot_param.leftMargin = 20;

        options_text.setLayoutParams(ot_param);


        activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
            gridLayout.addView(options_text);

        }  });

        int r = 1;
        for (int i = 0, c = 0; i < total; i++, c++) {
            if (c == column) {
                c = 0;
                r++;
            }

            final ImageButton new_button = new ImageButton(context);

            try{
                new_button.setImageResource(R.mipmap.class.getField(image_prefix + CategoryExtras.CleanForImage(names.get(i))).getInt("id"));
            }
            catch (Exception e)
            {
                Log.w(LogTag, "Failed to get image with name: " + image_prefix + CategoryExtras.CleanForImage(names.get(i)));
                new_button.setImageResource(R.mipmap.sandwich);
            }
            new_button.setBackgroundColor(ContextCompat.getColor(context, R.color.colorButton));
            new_button.setTag(R.string.button_id_tag, ids.get(i));
            new_button.setTag(R.string.button_name_tag, names.get(i));
            new_button.setTag(R.string.button_type_tag, types.get(i));
            new_button.setTag(R.string.button_price_tag, prices.get(i));
            new_button.setScaleType(ImageView.ScaleType.FIT_CENTER);
            new_button.setOnClickListener(listener);
            new_button.setPadding(0, 0, 0, 0);

            GridLayout.LayoutParams b_param = new GridLayout.LayoutParams();
            b_param.height = (int) context.getResources().getDimension(R.dimen.button_height);
            b_param.width = (int) context.getResources().getDimension(R.dimen.button_width);
            b_param.rightMargin = 20;
            b_param.leftMargin = 20;
            //b_param.topMargin = 0;
            b_param.setGravity(Gravity.CENTER);

            try {
                if (min_total < 3)
                    b_param.columnSpec = GridLayout.spec(c);
                else
                    b_param.columnSpec = GridLayout.spec(c, 1f);
            }
            catch (NoSuchMethodError e)
            {
                Log.w(LogTag, "Not using weight");
                b_param.columnSpec = GridLayout.spec(c);
            }
            b_param.rowSpec = GridLayout.spec(r*2);
            new_button.setLayoutParams(b_param);

            //final TextView new_text = new TextView(context);
            final CheckBox new_check = new CheckBox(context);

            String text_string = names.get(i);
            if (text_string.length() > 20)
            {
                text_string = text_string.substring(0, 20);
                Log.w(LogTag, "Text "+names.get(i)+" too long for button, truncating");
            }
            if (types.get(i).compareTo("fixed") != 0) {
                text_string += "\n  +$" + prices.get(i);
            }
            new_check.setText(text_string + "\n");
            new_check.setTextColor(Color.parseColor("#ffffff"));
            new_check.setGravity(Gravity.CENTER_HORIZONTAL);
            new_check.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            //ColorStateList check_colors = new ColorStateList(new int[][]{new int[0]}, new int[]{0xffffcc00});

            int[][] states = new int[][] {
                    new int[] { android.R.attr.state_enabled}, // enabled
                    //new int[] {-android.R.attr.state_enabled}, // disabled
                    //new int[] {-android.R.attr.state_checked}, // unchecked
                    //new int[] { android.R.attr.state_pressed}  // pressed
            };

            int[] colors;

            if(types.get(i).compareTo("optional") == 0) {
                colors = new int[]{
                        ContextCompat.getColor(context, R.color.colorPrimary),
                        //Color.RED,
                        //Color.GREEN,
                        //Color.BLUE
                };
            }
            else if (types.get(i).compareTo("fixed") == 0)
            {
                colors = new int[]{
                        ContextCompat.getColor(context, R.color.colorPrimaryDark),
                        //Color.RED,
                        //Color.GREEN,
                        //Color.BLUE
                };
            }
            else
            {
                colors = new int[]{
                        Color.WHITE,
                        //Color.RED,
                        //Color.GREEN,
                        //Color.BLUE
                };
                Log.e("Options Extras", "Illegal option type found: " + types.get(i));
            }

            ColorStateList check_colors = new ColorStateList(states, colors);

            try {
                new_check.setButtonTintList(check_colors);
            }
            catch (NoSuchMethodError e)
            {
                Log.w("Options Extras", "Changing checkbox color not supported in api < 21");
            }

            int checkbox_id = gridLayout.generateViewId();
            new_check.setId(checkbox_id);
            new_button.setTag(R.string.button_check_id_tag, checkbox_id);

            new_check.setTag(R.string.button_id_tag, ids.get(i));
            new_check.setTag(R.string.button_name_tag, names.get(i));
            new_check.setTag(R.string.button_type_tag, types.get(i));
            new_check.setTag(R.string.button_price_tag, prices.get(i));
            new_check.setTag(R.string.button_check_id_tag, checkbox_id);
            new_check.setOnClickListener(listener);

            GridLayout.LayoutParams t_param = new GridLayout.LayoutParams();
            t_param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            t_param.width = (int) context.getResources().getDimension(R.dimen.button_width);
            t_param.rightMargin = 5;
            t_param.topMargin = 5;
            t_param.bottomMargin = 20;

            try
            {
                if (min_total < 3)
                    t_param.columnSpec = GridLayout.spec(c);
                else
                    t_param.columnSpec = GridLayout.spec(c, 1f);
            }
            catch (NoSuchMethodError e)
            {
                t_param.columnSpec = GridLayout.spec(c);
            }
            t_param.rowSpec = GridLayout.spec(r * 2+1);
            new_check.setLayoutParams(t_param);

            // updating ui must be done on main thread
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (new_button.getTag(R.string.button_type_tag).toString().compareTo("fixed") == 0)
                        new_check.setChecked(true);

                    gridLayout.addView(new_button);
                    gridLayout.addView(new_check);
                }
            });

        }

        // "Meals:"
        final TextView meals_text = new TextView(context);
        meals_text.setText("Meals:");
        meals_text.setTextSize(20f);
        meals_text.setPadding(0, 0, 0, 0);
        meals_text.setTextColor(Color.parseColor("#ffffff"));
        meals_text.setGravity(Gravity.LEFT);
        //display_text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        GridLayout.LayoutParams mt_param = new GridLayout.LayoutParams();
        //mt_param.height = (int) context.getResources().getDimension(R.dimen.options_display_text_height);
        mt_param.width = (int) context.getResources().getDimension(R.dimen.button_width);
        //mt_param.width = (int) context.getResources().getDimension(R.dimen.button_width);


        try {
            if (min_total < 3)
                mt_param.columnSpec = GridLayout.spec(0);
            else
                mt_param.columnSpec = GridLayout.spec(0, 1f);
        }
        catch (NoSuchMethodError e)
        {
            Log.w(LogTag, "Not using weight");
            mt_param.columnSpec = GridLayout.spec(0);
        }
        //r  = r*2 + 1;

        r++;
        mt_param.rowSpec = GridLayout.spec(r*2+1);
        //mt_param.rightMargin = 20;
        //mt_param.leftMargin = 20;
        mt_param.topMargin = 20;
        mt_param.bottomMargin = 20;

        meals_text.setLayoutParams(mt_param);

        if (meal_total > 0) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gridLayout.addView(meals_text);

                }
            });
        }

        r++;

        for (int i = 0, c = 0; i < meal_total; i++, c++) {
            if (c == meal_column) {
                c = 0;
                r++;
            }

            final ImageButton new_meal_button = new ImageButton(context);

            try{
                // image prefix needs to be changed to "real_"
                new_meal_button.setImageResource(R.mipmap.class.getField(image_prefix + CategoryExtras.CleanForImage(meals_opt_names.get(i))).getInt("id"));
            }
            catch (Exception e)
            {
                Log.w(LogTag, "Failed to get image with name: " + image_prefix + CategoryExtras.CleanForImage(meals_opt_names.get(i)));
                new_meal_button.setImageResource(R.mipmap.sandwich);
            }
            new_meal_button.setBackgroundColor(ContextCompat.getColor(context, R.color.colorButton));
            new_meal_button.setTag(R.string.button_id_tag, meals_opt_ids.get(i));
            new_meal_button.setTag(R.string.button_name_tag, meals_opt_names.get(i));
            new_meal_button.setTag(R.string.button_type_tag, "optional");
            new_meal_button.setTag(R.string.button_price_tag, meals_prices.get(i));
            new_meal_button.setScaleType(ImageView.ScaleType.FIT_CENTER);
            new_meal_button.setOnClickListener(listener);
            new_meal_button.setPadding(0, 0, 0, 0);

            GridLayout.LayoutParams mb_param = new GridLayout.LayoutParams();
            mb_param.height = (int) context.getResources().getDimension(R.dimen.button_height);
            mb_param.width = (int) context.getResources().getDimension(R.dimen.button_width);
            mb_param.rightMargin = 20;
            mb_param.leftMargin = 20;
            //mb_param.topMargin = 0;
            mb_param.setGravity(Gravity.CENTER);

            try {
                if (min_total < 3)
                    mb_param.columnSpec = GridLayout.spec(c);
                else
                    mb_param.columnSpec = GridLayout.spec(c, 1f);
            }
            catch (NoSuchMethodError e)
            {
                Log.w(LogTag, "Not using weight");
                mb_param.columnSpec = GridLayout.spec(c);
            }
            mb_param.rowSpec = GridLayout.spec(r*2);
            new_meal_button.setLayoutParams(mb_param);

            //final TextView new_text = new TextView(context);
            final CheckBox new_meal_check = new CheckBox(context);

            String text_string = meals_opt_names.get(i);
            if (text_string.length() > 20)
            {
                text_string = text_string.substring(0, 20);
                Log.w(LogTag, "Text "+meals_opt_names.get(i)+" too long for button, truncating");
            }
            String size_string = meals_size_names.get(i);
            if (size_string.length() > 20)
            {
                size_string = size_string.substring(0, 20);
                Log.w(LogTag, "Text "+meals_size_names.get(i)+" too long for button, truncating");
            }

            text_string = size_string + "\n" + text_string + "\n  +$" + meals_prices.get(i);

            new_meal_check.setText(text_string + "\n");
            new_meal_check.setTextColor(Color.parseColor("#ffffff"));
            new_meal_check.setGravity(Gravity.CENTER_HORIZONTAL);
            new_meal_check.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            //ColorStateList check_colors = new ColorStateList(new int[][]{new int[0]}, new int[]{0xffffcc00});

            int[][] states = new int[][] {
                    new int[] { android.R.attr.state_enabled}, // enabled
                    //new int[] {-android.R.attr.state_enabled}, // disabled
                    //new int[] {-android.R.attr.state_checked}, // unchecked
                    //new int[] { android.R.attr.state_pressed}  // pressed
            };

            int[] colors;

            colors = new int[]{
                        ContextCompat.getColor(context, R.color.colorPrimary),
                        //Color.RED,
                        //Color.GREEN,
                        //Color.BLUE
            };

            ColorStateList check_colors = new ColorStateList(states, colors);

            try {
                new_meal_check.setButtonTintList(check_colors);
            }
            catch (NoSuchMethodError e)
            {
                Log.w("Options Extras", "Changing checkbox color not supported in api < 21");
            }

            int meal_checkbox_id = gridLayout.generateViewId();
            new_meal_check.setId(meal_checkbox_id);
            new_meal_button.setTag(R.string.button_check_id_tag, meal_checkbox_id);

            new_meal_check.setTag(R.string.button_id_tag, meals_opt_ids.get(i));
            new_meal_check.setTag(R.string.button_name_tag, meals_opt_names.get(i));
            new_meal_check.setTag(R.string.button_type_tag, "optional");
            new_meal_check.setTag(R.string.button_price_tag, meals_prices.get(i));
            new_meal_check.setTag(R.string.button_check_id_tag, meal_checkbox_id);
            new_meal_check.setOnClickListener(listener);

            GridLayout.LayoutParams mc_param = new GridLayout.LayoutParams();
            mc_param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            mc_param.width = (int) context.getResources().getDimension(R.dimen.button_width);
            mc_param.rightMargin = 5;
            mc_param.topMargin = 5;
            mc_param.bottomMargin = 20;

            try
            {
                if (min_total < 3)
                    mc_param.columnSpec = GridLayout.spec(c);
                else
                    mc_param.columnSpec = GridLayout.spec(c, 1f);
            }
            catch (NoSuchMethodError e)
            {
                mc_param.columnSpec = GridLayout.spec(c);
            }
            mc_param.rowSpec = GridLayout.spec(r * 2+1);
            new_meal_check.setLayoutParams(mc_param);

            // updating ui must be done on main thread
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gridLayout.addView(new_meal_button);
                    gridLayout.addView(new_meal_check);
                }
            });

        }
    }
}
