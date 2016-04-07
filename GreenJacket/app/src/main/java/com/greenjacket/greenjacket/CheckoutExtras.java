package com.greenjacket.greenjacket;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sammy Sam on 4/6/2016.
 */
public class CheckoutExtras {
    private Checkout checkout_activity;
    private Context checkout_context;

    CheckoutExtras(Context new_context, Checkout new_activity)
    {
        checkout_activity = new_activity;
        checkout_context = new_context;
    }


    public void CheckoutXButton(View view)
    {
        // delete and refresh/remove row
        int to_delete = (Integer) view.getTag(R.string.button_id_tag);

        // cache orders from main_activity for efficiency?
        JSONArray temp_orders = checkout_activity.main_activity.orders;
        JSONObject temp_row;

        try {
            for (int i = 0; i < temp_orders.length(); i++) {
                temp_row = (JSONObject) temp_orders.get(i);
                if (temp_row.getInt("row_id") == to_delete)
                {
                    //System.out.println("of " + checkout_activity.main_activity.orders);
                    //System.out.println("deleting " + to_delete);

                    // make sure to delete from original list
                    checkout_activity.main_activity.orders.remove(i);
                    //System.out.println("so now it's " + checkout_activity.main_activity.orders);

                    CreateCheckoutButtonsDo(checkout_activity.main_activity.orders);

                    View this_view = checkout_activity.findViewById(R.id.checkout_content);
                    Snackbar.make(this_view, "Item removed from cart", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    // you must return here and not continue for loop
                    // otherwise local and original list are out of sync
                    return;
                }
            }
        }
        catch (JSONException e)
        {
            Log.e("X button listener", "Error deleting an order: " + e);
        }

    }

    public String FormatMoney (float number) {
        float epsilon = 0.004f; // 4 tenths of a cent
        if (Math.abs(Math.round(number) - number) < epsilon) {
            return String.format("%.2f", number);
        } else {
            return String.format("%.2f", number);
        }
    }


    public void CreateCheckoutButtonsDo(JSONArray orders) throws JSONException
    {
        JSONObject temp_row;
        int temp_id;
        String temp_name;
        float temp_base_price;
        JSONArray temp_option_names;
        JSONArray temp_option_prices;
        String temp_option_names_str;

        ArrayList<Integer> ids = new ArrayList<Integer>();
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> prices = new ArrayList<String>();
        ArrayList<String> option_names = new ArrayList<String>();

        for (int i = 0; i < orders.length(); i++)
        {
            temp_row = (JSONObject) orders.get(i);

            temp_id = temp_row.getInt("row_id");
            temp_name = temp_row.getString("real_name");
            temp_base_price = Float.parseFloat(temp_row.getString("size_price"));
            // remember options prices ================================================================================================
            temp_option_names = temp_row.getJSONArray("chosen_option_names");
            temp_option_prices = temp_row.getJSONArray("chosen_option_prices");

            temp_option_names_str = new String();

            for (int j = 0; j < temp_option_names.length(); j++)
            {
                temp_option_names_str += "  +" + temp_option_names.get(j);

                if (j != temp_option_names.length() - 1)
                    temp_option_names_str +=  ", ";

                temp_base_price += Float.parseFloat((String) temp_option_prices.get(j));
            }

            // if length is too long, put options on different lines
            if (temp_option_names_str.length() > 38)
            {
                temp_option_names_str = "";
                //temp_option_names_str = temp_option_names_str.substring(0, 38);

                for (int j = 0; j < temp_option_names.length(); j++)
                {
                    temp_option_names_str += "  +" + temp_option_names.get(j) + "\n";
                }
            }

            option_names.add(temp_option_names_str);

            ids.add(temp_id);
            names.add(temp_name);
            prices.add(FormatMoney(temp_base_price));
        }


        View.OnClickListener checkout_listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CheckoutXButton(v);
            }
        };

        GridLayout grid_layout = (GridLayout) checkout_activity.findViewById(R.id.checkout_content);

        CreateCheckoutButtons(ids, names, prices, option_names, checkout_listener,
                "real_", grid_layout, checkout_context,
                checkout_activity);
    }


    public void CreateCheckoutButtons(ArrayList<Integer> ids, ArrayList<String> names,
                                      ArrayList<String> prices, ArrayList<String> option_names,
                                      View.OnClickListener listener, String image_prefix,
                                      final GridLayout gridLayout, Context context,
                                      Checkout checkout_activity)
    {
        String LogTag = "create checkout";
        gridLayout.removeAllViews();
        int total = ids.size();
        int column = 4;
        int row = total;
        gridLayout.setColumnCount(column);
        gridLayout.setRowCount(row);

        String real_name;
        String real_price;

        for (int i = 0, r = 0; i < total; i++, r++)
        {
            real_name = names.get(i);
            real_price = prices.get(i);

            final ImageView new_img = new ImageView(context);
            try{
                new_img.setImageResource(R.mipmap.class.getField(image_prefix
                        + CategoryExtras.CleanForImage(real_name)).getInt("id"));
            }
            catch (Exception e)
            {
                Log.w(LogTag, "Failed to get image with name: " + image_prefix + CategoryExtras.CleanForImage(real_name));
                new_img.setImageResource(R.mipmap.real_big_mac);
            }


            // create image
            new_img.setScaleType(ImageView.ScaleType.FIT_CENTER);
            new_img.setPadding(0, 0, 0, 0);

            GridLayout.LayoutParams i_param = new GridLayout.LayoutParams();
            i_param.height = (int) context.getResources().getDimension(R.dimen.checkout_real_image_height);
            i_param.width = (int) context.getResources().getDimension(R.dimen.checkout_real_image_width);
            i_param.setGravity(Gravity.LEFT);


            try {
                i_param.columnSpec = GridLayout.spec(0, 1f);
            }
            catch (NoSuchMethodError e)
            {
                Log.w(LogTag, "Not using weight");
                i_param.columnSpec = GridLayout.spec(0);
            }
            i_param.rowSpec = GridLayout.spec(r);
            new_img.setLayoutParams(i_param);


            // Create item name
            final TextView item_name = new TextView(context);
            if (real_name.length() > 10)
            {
                Log.w(LogTag, "Text "+real_name+" too long for display: truncating");
                real_name = real_name.substring(0, 10);
            }
            real_name += "\n" + option_names.get(i);

            item_name.setText(real_name);
            item_name.setTextSize(14f);
            item_name.setPadding(30, 0, 0, 0);
            item_name.setTextColor(Color.parseColor("#ffffff"));
            item_name.setGravity(Gravity.TOP);

            GridLayout.LayoutParams t_param = new GridLayout.LayoutParams();
            t_param.height = (int) context.getResources().getDimension(R.dimen.checkout_name_height);

            try {
                t_param.columnSpec = GridLayout.spec(1, 2f);
            }
            catch (NoSuchMethodError e)
            {
                Log.w(LogTag, "Not using weight");
                t_param.columnSpec = GridLayout.spec(1);
            }

            t_param.rowSpec = GridLayout.spec(r);
            item_name.setLayoutParams(t_param);

            // Create item price
            final TextView item_price = new TextView(context);
            if (real_price.length() > 6)
            {
                Log.w(LogTag, "Text "+real_price+" too long for display: truncating");
                real_price = real_price.substring(0, 6);
            }
            real_price = "$" + real_price;

            item_price.setText(real_price);
            item_price.setTextSize(12f);
            item_price.setPadding(0, 0, 20, 0);
            item_price.setTextColor(Color.parseColor("#ffffff"));
            item_price.setGravity(Gravity.CENTER_VERTICAL);

            GridLayout.LayoutParams p_param = new GridLayout.LayoutParams();
            p_param.height = (int) context.getResources().getDimension(R.dimen.checkout_price_height);

            try {
                p_param.columnSpec = GridLayout.spec(2, 1f);
            }
            catch (NoSuchMethodError e)
            {
                Log.w(LogTag, "Not using weight");
                item_price.setPadding(20, 0, 20, 0);
                p_param.columnSpec = GridLayout.spec(2);
            }

            p_param.rowSpec = GridLayout.spec(r);
            item_price.setLayoutParams(p_param);


            // create X button
            final ImageButton new_button = new ImageButton(context);

            try{
                new_button.setImageResource(R.mipmap.class.getField("x_button").getInt("id"));
            }
            catch (Exception e)
            {
                Log.w(LogTag, "Failed to get image with name: x_button");
                new_button.setImageResource(R.mipmap.x_button);
            }
            new_button.setBackgroundColor(ContextCompat.getColor(context, R.color.colorButton));
            new_button.setTag(R.string.button_id_tag, ids.get(i));
            new_button.setScaleType(ImageView.ScaleType.FIT_CENTER);
            new_button.setOnClickListener(listener);
            new_button.setPadding(0, 10, 0, 10);

            GridLayout.LayoutParams b_param = new GridLayout.LayoutParams();
            b_param.height = (int) context.getResources().getDimension(R.dimen.x_button_height);
            b_param.width = (int) context.getResources().getDimension(R.dimen.x_button_width);
            //b_param.topMargin = 60;
            b_param.setGravity(Gravity.CENTER);

            try {
                b_param.columnSpec = GridLayout.spec(3, .4f);
            }
            catch (NoSuchMethodError e)
            {
                Log.w(LogTag, "Not using weight");
                b_param.columnSpec = GridLayout.spec(3);
            }
            b_param.rowSpec = GridLayout.spec(r);
            new_button.setLayoutParams(b_param);



            checkout_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gridLayout.addView(new_img);
                    gridLayout.addView(item_name);
                    gridLayout.addView(item_price);
                    gridLayout.addView(new_button);
                }
            });


        }




    }
}
