package com.example.dealerapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodaysHistory extends AppCompatActivity {

    ListView listView;
    TextView total_bets, winnings, profit_loss;
    String player_id, week;
    int bet=0, win=0, prftlos=0;
    List<HistoryGetSet> historyList = new ArrayList<>();
    public TodaysHistory() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_todays_history);

        listView = (ListView) findViewById(R.id.list);
        total_bets = (TextView) findViewById(R.id.txttotalbets);
        winnings = (TextView) findViewById(R.id.txtwinnings);
        profit_loss = (TextView) findViewById(R.id.txtprftlos);

        player_id = this.getIntent().getStringExtra("player_id");
        week = this.getIntent().getStringExtra("week");

        getHistory();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HistoryGetSet item = historyList.get(i);
               startActivity(new Intent(TodaysHistory.this, TodaysSummary.class).putExtra("date",item.getDate()));
            }
        });

    }

    private void getHistory()
    {
        ConnectionDetector connectionDetector = new ConnectionDetector(TodaysHistory.this);
        if(connectionDetector.isConnectingToInternet()) {
            String tag_string_req = "string_req";

            try {

             String url = getString(R.string.get_history_by_week) + player_id+"&week="+week;
                Log.i("url", "" + url);
            final ProgressDialog pDialog = new ProgressDialog(TodaysHistory.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            final String TAG = "login";
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    pDialog.hide();
                    try {
                        Log.i("response", "" + response);
                        if(response != null)
                        {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("status").equals("true"))
                            {
                                JSONObject jsonObject1= jsonObject.getJSONObject("data");
                                JSONArray jsonArray = jsonObject1.getJSONArray("data_weekly");
                                for(int i=0; i < jsonArray.length(); i++)
                                {
                                    JSONObject item = jsonArray.getJSONObject(i);
                                    int pl=0;
                                    HistoryGetSet rowItem = new HistoryGetSet();
                                    rowItem.setAmount(item.getString("bet_amount"));
                                    rowItem.setDate(item.getString("date"));
                                    rowItem.setTotal_bet(item.getString("total_bet"));
                                    rowItem.setTotal_wins(item.getString("total_wins"));
                                    rowItem.setPayout(item.getString("payout"));

                                    String wins = item.getString("payout");
                                    wins=wins.replace(",","");
                                    String bets= item.getString("bet_amount");
                                    bets=bets.replace(",","");
                                    if(!wins.equals("null")) {
                                        pl = (int) Math.round(Double.parseDouble(wins)) - (int) Math.round(Double.parseDouble(bets));
                                        rowItem.setProftlos(String.valueOf(pl));
                                        String ttlbet =item.getString("bet_amount");
                                        ttlbet = ttlbet.replace(",","");
                                        bet+= (int) Math.round(Double.parseDouble(ttlbet));
                                        String ttlwin =item.getString("payout");
                                        ttlwin = ttlwin.replace(",","");
                                        win+= (int) Math.round(Double.parseDouble(ttlwin));
                                        prftlos+= pl;
                                    }

                                        historyList.add(rowItem);

                                }
                                HistoryAdapter adapter = new HistoryAdapter(getApplicationContext(),historyList);
                                listView.setAdapter(adapter);
                                total_bets.setText(""+bet);
                                winnings.setText(""+win);
                                profit_loss.setText(""+prftlos);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Something went wrong, please try again!!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (Exception e) {
                        pDialog.hide();
                        Toast.makeText(getApplicationContext(), "Something went wrong please try again!!!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), "History Not Present!!!", Toast.LENGTH_SHORT).show();
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
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please check internet connetion!!!", Toast.LENGTH_SHORT).show();
        }
    }

}
