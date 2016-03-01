package com.greenjacket.greenjacket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;

public class Options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Options");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Context options_context = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_category = new Intent(options_context, MainActivity.class);
                to_category.putExtra("item_added", true);
                startActivity(to_category);
            }
        });
    }

    public void OptionsBaconButton(View view) {
        // Do something in response to button
        final CheckBox checkBox = (CheckBox) findViewById(R.id.bacon_checkbox);
        checkBox.toggle();
    }

    public void OptionsPicklesButton(View view) {
        // Do something in response to button
        final CheckBox checkBox = (CheckBox) findViewById(R.id.pickles_checkbox);
        checkBox.toggle();
    }
}
