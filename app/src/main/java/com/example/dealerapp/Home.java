package com.example.dealerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Home extends AppCompatActivity {

    private ListView list;
    private static String [] List={"Cricket","Lottery","Combined"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list=(ListView) findViewById(R.id.listview);
        list.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, List));

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position==0){
                        startActivity(new Intent(getApplicationContext(), TabContainer.class).putExtra("url",getString(R.string.url_players_lottery)));

                    }
                if(position==1){
                    startActivity(new Intent(getApplicationContext(), TabContainer.class).putExtra("url",getString(R.string.url_players_cricket)));

                }
                if(position==2){
                    startActivity(new Intent(getApplicationContext(), TabContainer.class).putExtra("url",getString(R.string.url_players_lottery)));

                }
            }
        });

    }

}
