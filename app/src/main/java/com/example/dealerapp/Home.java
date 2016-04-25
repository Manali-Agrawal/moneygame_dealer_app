package com.example.dealerapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Home extends AppCompatActivity {

    private ListView list;
    public static int flag=0;
    private static String [] List={"Cricket","Lottery","Combined"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list=(ListView) findViewById(R.id.listview);
        list.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.child_game_list, R.id.textView, List));

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position==0){
                        flag=0;
                        startActivity(new Intent(getApplicationContext(), TabContainer.class).putExtra("url",getString(R.string.url_players_cricket)));

                    }
                if(position==1){
                    flag=1;
                    startActivity(new Intent(getApplicationContext(), TabContainer.class).putExtra("url",getString(R.string.url_players_lottery)));

                }
                if(position==2){
                    flag=2;
                    startActivity(new Intent(getApplicationContext(), TabContainer.class).putExtra("url",getString(R.string.url_players_combined)));

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPreferences settings = getSharedPreferences(getString(R.string.prefrence), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove("logged");
            editor.commit();
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
