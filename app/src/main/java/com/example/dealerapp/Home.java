package com.example.dealerapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity implements View.OnClickListener {

    private CardView mLotteryCard,mCricketCard,mCombined, mLogout;
    private TextView user;
    public static int flag=0;
    String user_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user=(TextView) findViewById(R.id.textuser);

        user_code= getSharedPreferences(getString(R.string.prefrence), Context.MODE_PRIVATE).getString("username", "");

        user.setText("Welcome, "+user_code+"!");

        mLotteryCard=(CardView) findViewById(R.id.lottery);
        mCricketCard=(CardView) findViewById(R.id.cricket);
        mCombined=(CardView) findViewById(R.id.combined);
        mLogout=(CardView) findViewById(R.id.logout);

        mLotteryCard.setCardBackgroundColor(Color.parseColor("#8c9e90"));
        mCricketCard.setCardBackgroundColor(Color.parseColor("#12132f"));
        mCombined.setCardBackgroundColor(Color.parseColor("#710302"));
        mLogout.setCardBackgroundColor(Color.parseColor("#CDAF95"));

        mLotteryCard.setOnClickListener(this);
        mCricketCard.setOnClickListener(this);
        mCombined.setOnClickListener(this);
        mLogout.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cricket:
                flag=0;
                startActivity(new Intent(getApplicationContext(), TabContainer.class).putExtra("url",getString(R.string.url_players_cricket)));
                break;

            case R.id.lottery:
                flag=1;
                startActivity(new Intent(getApplicationContext(), TabContainer.class).putExtra("url",getString(R.string.url_players_lottery)));
                break;

            case R.id.combined:
                flag=2;
                startActivity(new Intent(getApplicationContext(), TabContainer.class).putExtra("url",getString(R.string.url_players_combined)));
                break;

            case R.id.logout:
                ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                        connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

                    AlertDialog.Builder adb = new AlertDialog.Builder(this);
                    adb.setTitle("Log Out");
                    adb.setMessage("Are you sure?");
                    adb.setIcon(android.R.drawable.ic_dialog_alert);

                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences settings = getSharedPreferences(getString(R.string.prefrence), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.remove("logged");
                            editor.commit();
                            finish();
                            Intent i = new Intent(Home.this, Login.class);
                            startActivity(i);
                            Toast.makeText(Home.this, "Successfully Logged Out..", Toast.LENGTH_SHORT).show();
                        }
                    });
                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });
                    adb.show();
                }else{
                    Toast.makeText(Home.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
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
            ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                    connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle("Log Out");
                adb.setMessage("Are you sure?");
                adb.setIcon(android.R.drawable.ic_dialog_alert);

                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences settings = getSharedPreferences(getString(R.string.prefrence), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.remove("logged");
                        editor.commit();
                        finish();
                        Intent i = new Intent(Home.this, Login.class);
                        startActivity(i);
                        Toast.makeText(Home.this, "Successfully Logged Out..", Toast.LENGTH_SHORT).show();
                    }
                });
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                adb.show();
            }else{
                Toast.makeText(Home.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
