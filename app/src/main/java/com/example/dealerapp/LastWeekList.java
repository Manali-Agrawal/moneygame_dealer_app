package com.example.dealerapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by root on 4/25/16.
 */
public class LastWeekList extends Activity {

    ListView list;
    String url, dealer_id, user_code, week;
    private List<WeekListGetSet> weeklist = new ArrayList<>();
    private WeekListAdapter adapter;
    public SharedPreferences preferences;
    TextView ttlchips, ttlwins, ttlcomm, netblnc;
    int bet=0, win=0, prftlos=0, net=0;
    String chip, wins, comm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_list);

        dealer_id= getSharedPreferences(getString(R.string.prefrence), Context.MODE_PRIVATE).getString("dealer_id", "");
        user_code= getSharedPreferences(getString(R.string.prefrence), Context.MODE_PRIVATE).getString("username", "");

        list=(ListView) findViewById(R.id.weeklist);

        ttlchips=(TextView) findViewById(R.id.txttotalbets);
        ttlwins=(TextView) findViewById(R.id.txtwinnings);
        ttlcomm=(TextView) findViewById(R.id.txtprftlos);
        netblnc=(TextView) findViewById(R.id.txtnet);

        url= this.getIntent().getStringExtra("url");

        preferences=getSharedPreferences(getString(R.string.prefrence), MODE_PRIVATE);

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        int sunday=calendar.get(Calendar.DAY_OF_WEEK);
        String[] days = new String[7];
        if (sunday==Calendar.SUNDAY){
            calendar.add(Calendar.WEEK_OF_MONTH,-2);
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }
        else{
            calendar.add(Calendar.WEEK_OF_MONTH, -1);
        }
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        for (int i = 0; i < 7; i++)
        {
            days[i] = format.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        week= days[0]+"%20To%20"+days[6];

        getWeeklist(url+dealer_id+"&week="+week);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(Home.flag==0) {
                    WeekListGetSet item = weeklist.get(i);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("player_id",item.getPlayer_id());
                    editor.commit();
                    startActivity(new Intent(LastWeekList.this, CricketAccounts.class).putExtra("player_id", item.getPlayer_id()).putExtra("week", week));
                }
                else if(Home.flag==1){
                    WeekListGetSet item = weeklist.get(i);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("player_id",item.getPlayer_id());
                    editor.commit();
                    startActivity(new Intent(LastWeekList.this, TodaysHistory.class).putExtra("player_id", item.getPlayer_id()).putExtra("week", week));
                }
                else if(Home.flag==2){
                    WeekListGetSet item = weeklist.get(i);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("player_id",item.getPlayer_id());
                    editor.commit();
                    startActivity(new Intent(LastWeekList.this, CombinedAccounts.class).putExtra("player_id", item.getPlayer_id()).putExtra("week", week));
                }
            }
        });

    }

    private void getWeeklist(String url)
    {
        ConnectionDetector connectionDetector = new ConnectionDetector(getApplicationContext());
        if(connectionDetector.isConnectingToInternet()) {
            String tag_string_req = "string_req";

            Log.i("url", url);
            final ProgressDialog pDialog = new ProgressDialog(LastWeekList.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            final String TAG = "login";
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    pDialog.hide();
                    try
                    {
                        Log.i("res", "" + response);
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getString("status").equals("true")) {
                            JSONObject object = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = object.getJSONArray("data_weekly");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    WeekListGetSet item = new WeekListGetSet();
                                    JSONObject trnsaction = jsonArray.getJSONObject(i);

                                    item.setPlayer_nm(trnsaction.getString("user_code"));
                                    item.setPlayer_id(trnsaction.getString("player_id"));
                                    item.setChips(trnsaction.getString("bet_amount"));
                                    if (trnsaction.getString("bet_amount").equals("null")) {
                                        item.setChips("0");
                                        chip="0";
                                    } else {
                                        item.setChips(trnsaction.getString("bet_amount"));
                                        chip=trnsaction.getString("bet_amount");
                                    }
                                    if (trnsaction.getString("payout").equals("null")) {
                                        item.setWins("0");
                                        wins="0";
                                    } else {
                                        item.setWins(trnsaction.getString("payout"));
                                        wins=trnsaction.getString("payout");
                                    }

                                    if (trnsaction.getString("commission").equals("null")) {
                                        item.setComm("0");
                                        comm="0";
                                    } else {
                                        item.setComm(trnsaction.getString("commission"));
                                        comm=trnsaction.getString("commission");
                                    }

                                    bet+= (int) Math.round(Double.parseDouble(chip));
                                    win+= (int) Math.round(Double.parseDouble(wins));
                                    prftlos+= (int) Math.round(Double.parseDouble(comm));
                                    weeklist.add(item);
                                }
                                net = bet-win-prftlos;
                                adapter = new WeekListAdapter(getApplicationContext(), weeklist);
                                list.setAdapter(adapter);
                                ttlchips.setText("" + bet);
                                ttlwins.setText(""+win);
                                ttlcomm.setText(""+prftlos);
                                netblnc.setText("Net Balance : "+net);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Something went wrong please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.hide();
                    if (error instanceof TimeoutError) {
                        Toast.makeText(getApplicationContext(), "Request Timeout!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_SHORT).show();
                    }
                    error.printStackTrace();
                    VolleyLog.d(TAG, "Error: " + error.getMessage());

                }
            });
            strReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Adding request to request queue
            AppControler.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please check internet connection!!!", Toast.LENGTH_SHORT).show();
        }
    }


}

