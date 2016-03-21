package com.greenjacket.greenjacket;

import android.content.Context;

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

}
