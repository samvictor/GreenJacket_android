package com.greenjacket.greenjacket;

import android.content.Context;

/**
 * Created by Sammy Sam on 3/26/2016.
 */
public class ContainerExtras {
    Container container_activity;
    Context container_context;

    public ContainerExtras (Context new_context, Container new_main_activity) {
        //setContentView(R.layout.activity_main);
        container_activity = new_main_activity;
        container_context = new_context;
    }
}
