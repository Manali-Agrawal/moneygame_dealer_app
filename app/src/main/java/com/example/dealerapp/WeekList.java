package com.example.dealerapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class WeekList extends Activity {

    ListView list;
    String url, dealer_id, user_code, week;
    private List<WeekListGetSet> weeklist = new ArrayList<>();
    private WeekListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_list);

        dealer_id= getSharedPreferences(getString(R.string.prefrence), Context.MODE_PRIVATE).getString("dealer_id", "");
        user_code= getSharedPreferences(getString(R.string.prefrence), Context.MODE_PRIVATE).getString("username", "");

        list=(ListView) findViewById(R.id.weeklist);

        url= this.getIntent().getStringExtra("url");

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        int sunday=calendar.get(Calendar.DAY_OF_WEEK);
        String[] days = new String[7];
        if (sunday==Calendar.SUNDAY){
            calendar.add(Calendar.DAY_OF_MONTH,-6);
        }
        for (int i = 0; i < 7; i++)
        {
            days[i] = format.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        String week= days[0]+"%20To%20"+days[6];

        getWeeklist(url+dealer_id+"&week="+week);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(Home.flag==0) {
                    WeekListGetSet item = weeklist.get(i);
                    startActivity(new Intent(WeekList.this, CricketAccounts.class).putExtra("player_id", item.getPlayer_nm()));
                }
                else if(Home.flag==1){
                    WeekListGetSet item = weeklist.get(i);
                    startActivity(new Intent(WeekList.this, TodaysHistory.class).putExtra("player_id", item.getPlayer_nm()));
                };

            }
        });

    }

    private void getWeeklist(String url)
    {
        ConnectionDetector connectionDetector = new ConnectionDetector(getApplicationContext());
        if(connectionDetector.isConnectingToInternet()) {
            String tag_string_req = "string_req";

            Log.i("url", url);
            final ProgressDialog pDialog = new ProgressDialog(WeekList.this);
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
                                    item.setChips(trnsaction.getString("bet_amount"));
                                    if (trnsaction.getString("bet_amount").equals("null")) {
                                        item.setChips("0");
                                    } else {
                                        item.setChips(trnsaction.getString("bet_amount"));
                                    }
                                    if (trnsaction.getString("payout").equals("null")) {
                                        item.setWins("0");
                                    } else {
                                        item.setWins(trnsaction.getString("payout"));
                                    }

                                    if (trnsaction.getString("commission").equals("null")) {
                                        item.setComm("0");
                                    } else {
                                        item.setComm(trnsaction.getString("commission"));
                                    }

                                    weeklist.add(item);
                                }
                                adapter = new WeekListAdapter(getApplicationContext(), weeklist);
                                list.setAdapter(adapter);
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
